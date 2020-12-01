package controllers

import (
	"context"

	"github.com/go-logr/logr"
	"k8s.io/apimachinery/pkg/runtime"
	ctrl "sigs.k8s.io/controller-runtime"
	"sigs.k8s.io/controller-runtime/pkg/client"

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

func (r *ManagedKafkaConnectionReconciler) Reconcile(req ctrl.Request) (ctrl.Result, error) {
	_ = context.Background()
	_ = r.Log.WithValues("managedkafkaconnection", req.NamespacedName)

	// your logic here

	return ctrl.Result{}, nil
}

func (r *ManagedKafkaConnectionReconciler) SetupWithManager(mgr ctrl.Manager) error {
	return ctrl.NewControllerManagedBy(mgr).
		For(&rhoasv1.ManagedKafkaConnection{}).
		Complete(r)
}
