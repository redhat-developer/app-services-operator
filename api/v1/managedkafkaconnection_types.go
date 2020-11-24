package v1

import (
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
)

// EDIT THIS FILE!  THIS IS SCAFFOLDING FOR YOU TO OWN!
// NOTE: json tags are required.  Any new fields you add must have json tags for the fields to be serialized.

// ManagedKafkaConnectionSpec defines the desired state of ManagedKafkaConnection
type ManagedKafkaConnectionSpec struct {
	// INSERT ADDITIONAL SPEC FIELDS - desired state of cluster
	// Important: Run "make" to regenerate code after modifying this file

	BootstrapServer BootstrapServerSpec `json:"bootstrapServer"`
	Credentials     CredentialsSpec     `json:"credentials"`
}

// BootstrapServerSpec contains server host information
type BootstrapServerSpec struct {
	Host string `json:"host,omitempty"`
}

// CredentialType enumeration for types of credentails
type CredentialType string

const (
	// ClientCredentials ... type
	ClientCredentials CredentialType = "ClientCredentials"
)

// CredentialsSpec specification
type CredentialsSpec struct {
	Kind         CredentialType `json:"kind,omitempty"`
	CientID      string         `json:"clientID,omitempty"`
	ClientSecret string         `json:"clientSecret,omitempty"`
}

// ManagedKafkaConnectionStatus defines the observed state of ManagedKafkaConnection
type ManagedKafkaConnectionStatus struct {
	// INSERT ADDITIONAL STATUS FIELD - define observed state of cluster
	// Important: Run "make" to regenerate code after modifying this file
}

// +kubebuilder:object:root=true
// +kubebuilder:subresource:status
// +kubebuilder:resource:scope=Cluster

// ManagedKafkaConnection is the Schema for the managedkafkaconnections API
type ManagedKafkaConnection struct {
	metav1.TypeMeta   `json:",inline"`
	metav1.ObjectMeta `json:"metadata,omitempty"`

	Spec   ManagedKafkaConnectionSpec   `json:"spec,omitempty"`
	Status ManagedKafkaConnectionStatus `json:"status,omitempty"`
}

// +kubebuilder:object:root=true

// ManagedKafkaConnectionList contains a list of ManagedKafkaConnection
type ManagedKafkaConnectionList struct {
	metav1.TypeMeta `json:",inline"`
	metav1.ListMeta `json:"metadata,omitempty"`
	Items           []ManagedKafkaConnection `json:"items"`
}

func init() {
	SchemeBuilder.Register(&ManagedKafkaConnection{}, &ManagedKafkaConnectionList{})
}
