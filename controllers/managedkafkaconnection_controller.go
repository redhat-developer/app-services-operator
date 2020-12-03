package controllers

import (
	"context"

	appsv1 "k8s.io/api/apps/v1"
	corev1 "k8s.io/api/core/v1"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	types "k8s.io/apimachinery/pkg/types"

	ctrl "sigs.k8s.io/controller-runtime"

	"github.com/go-logr/logr"
	"github.com/pingcap/errors"
	"k8s.io/apimachinery/pkg/runtime"
	"sigs.k8s.io/controller-runtime/pkg/client"

	rhoasv1 "github.com/bf2fc6cc711aee1a0c2a/operator/api/v1"
)

// ManagedKafkaConnectionReconciler reconciles a ManagedKafkaConnection object
type ManagedKafkaConnectionReconciler struct {
	Client client.Client
	Log    logr.Logger
	Scheme *runtime.Scheme
}

// +kubebuilder:rbac:groups=rhoas.redhat.com,resources=managedkafkaconnections,verbs=get;list;watch;create;update;patch;delete
// +kubebuilder:rbac:groups=rhoas.redhat.com,resources=managedkafkaconnections/status,verbs=get;update;patch
// +kubebuilder:rbac:groups=rhoas.redhat.com,resources=deployments,verbs=create;update;patch;delete

// Reconcile reconcilation logic
func (r *ManagedKafkaConnectionReconciler) Reconcile(req ctrl.Request) (ctrl.Result, error) {
	ctx := context.Background()
	log := r.Log.WithValues("managedkafkaconnection", req.NamespacedName)
	mkConnection := &rhoasv1.ManagedKafkaConnection{}
	// Read connection
	if err := r.Client.Get(ctx, req.NamespacedName, mkConnection); err != nil {
		// we'll ignore not-found errors, since they can't be fixed by an immediate
		// requeue (we'll need to wait for a new notification), and we can get them
		// on deleted requests.
		return ctrl.Result{}, client.IgnoreNotFound(err)
	}

	// Validate spec
	if mkConnection.Spec.Credentials.Type != "" && mkConnection.Spec.Credentials.Type != "ClientCredentialsSecret" {
		message := "Spec supports only ClientCredentialsSecret. Please validate your spec"
		log.Info(message)
		return ctrl.Result{}, errors.New(message)
	}
	if mkConnection.Spec.BootstrapServer.Host == "" {
		message := "Spec.BootstrapServer.Host missing. Please validate your spec"
		log.Info(message)
		return ctrl.Result{}, errors.New(message)
	}
	namespace := req.NamespacedName.Namespace
	// Build deployment
	deployment := newDeploymentForCR(mkConnection, namespace)
	// Set Database instance as the owner and controller
	if err := ctrl.SetControllerReference(mkConnection, deployment, r.Scheme); err != nil {
		return ctrl.Result{}, err
	}

	// Check if this Secret already exists
	secretFound := &corev1.Secret{}
	err := r.Client.Get(context.TODO(), types.NamespacedName{Name: mkConnection.Spec.Credentials.SecretName, Namespace: namespace}, secretFound)
	if err != nil && errors.IsNotFound(err) {
		log.Info("Cannot find specified secret: " + mkConnection.Spec.Credentials.SecretName)
		return ctrl.Result{}, nil
	} else if err != nil {
		return ctrl.Result{}, err
	}

	// Create deployment
	deploymentFound := &appsv1.Deployment{}
	err = r.Client.Get(context.TODO(), types.NamespacedName{Name: deployment.Name, Namespace: namespace}, deploymentFound)
	if err != nil && errors.IsNotFound(err) {
		log.Info("--------- Creating a new Deployment", "Deployment.Namespace", deployment.Namespace, "Deployment.Name", deployment.Name)
		err = r.Client.Create(context.TODO(), deployment)
		if err != nil {
			log.Error(err, "Problem with creating deployment")
			return ctrl.Result{}, err
		}
		deploymentFound = deployment
		return ctrl.Result{Requeue: true}, nil
	} else if err != nil {
		return ctrl.Result{Requeue: false}, err
	}

	// Update status
	mkConnection.Status.DeploymentName = deploymentFound.Name
	mkConnection.Status.SecretName = mkConnection.Spec.Credentials.SecretName

	err = r.Client.Status().Update(context.TODO(), mkConnection)
	if err != nil {
		log.Error(err, "Failed to update status with DBConfigMap")
		return ctrl.Result{}, err
	}

	return ctrl.Result{}, nil
}

func newDeploymentForCR(cr *rhoasv1.ManagedKafkaConnection, namespace string) *appsv1.Deployment {
	labels := map[string]string{
		"app": cr.Name,
	}
	containerPorts := []corev1.ContainerPort{{
		ContainerPort: 9000,
		Protocol:      corev1.ProtocolTCP,
	}}
	deployment := &appsv1.Deployment{
		ObjectMeta: metav1.ObjectMeta{
			Name:      cr.Name + "-deployment",
			Namespace: namespace,
			Labels:    labels,
		},
		Spec: appsv1.DeploymentSpec{
			Strategy: appsv1.DeploymentStrategy{
				Type: "Recreate",
			},
			// Replicas: int32Ptr(1),
			Selector: &metav1.LabelSelector{
				MatchLabels: labels,
			},
			Template: corev1.PodTemplateSpec{
				ObjectMeta: metav1.ObjectMeta{
					Name:      cr.Name + "deployment",
					Namespace: cr.Namespace,
					Labels:    labels,
				},
				Spec: corev1.PodSpec{
					Containers: []corev1.Container{
						{
							Name:            "noop",
							Image:           "wtrocki/noop:1.0.0",
							ImagePullPolicy: corev1.PullIfNotPresent,
							Ports:           containerPorts,
							Env:             []corev1.EnvVar{},
						},
					},
				},
			},
		},
	}
	return deployment
}

// SetupWithManager Setup logic
func (r *ManagedKafkaConnectionReconciler) SetupWithManager(mgr ctrl.Manager) error {
	return ctrl.NewControllerManagedBy(mgr).
		For(&rhoasv1.ManagedKafkaConnection{}).
		Owns(&appsv1.Deployment{}).
		Complete(r)
}
