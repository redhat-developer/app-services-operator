package controllers

import (
	"context"

	appsv1 "k8s.io/api/apps/v1"
	corev1 "k8s.io/api/core/v1"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"

	"github.com/go-logr/logr"
	"k8s.io/apimachinery/pkg/runtime"
	ctrl "sigs.k8s.io/controller-runtime"
	"sigs.k8s.io/controller-runtime/pkg/client"
	"sigs.k8s.io/controller-runtime/pkg/controller/controllerutil"
	"sigs.k8s.io/controller-runtime/pkg/reconcile"

	rhoasv1 "github.com/bf2fc6cc711aee1a0c2a/operator/api/v1"
)

// ManagedKafkaConnectionReconciler reconciles a ManagedKafkaConnection object
type ManagedKafkaConnectionReconciler struct {
	client.Client
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
	if err := r.Get(ctx, req.NamespacedName, mkConnection); err != nil {
		log.Error(err, "unable to fetch Managed Kafka")
		// we'll ignore not-found errors, since they can't be fixed by an immediate
		// requeue (we'll need to wait for a new notification), and we can get them
		// on deleted requests.
		return ctrl.Result{}, client.IgnoreNotFound(err)
	}

	deployment := newDeploymentForCR(mkConnection)
	// Set Database instance as the owner and controller
	if err := controllerutil.SetControllerReference(mkConnection, deployment, r.Scheme); err != nil {
		return reconcile.Result{}, err
	}

	return ctrl.Result{}, nil
}

func newDeploymentForCR(cr *rhoasv1.ManagedKafkaConnection) *appsv1.Deployment {
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
			Namespace: cr.Namespace,
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
					Name:      cr.Name + "-deployment",
					Namespace: cr.Namespace,
					Labels:    labels,
				},
				Spec: corev1.PodSpec{
					Containers: []corev1.Container{
						{
							// TODO
							Name:            "",
							Image:           "",
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
