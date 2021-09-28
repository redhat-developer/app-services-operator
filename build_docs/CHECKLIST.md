# Reviewing SBO integration with App Services

This checklist is designed to help developers to track source of the problem and properly report issue to the team.
When reporting issue please refer what step failed and supply relevant logs that our team will use to address that.

## Context

RHOAS SBO integration can be executed from CLI or OpenShift console.
Steps for both can differ but results should be the same. 

Integration uses two Operators (app-services and SBO operators) and end user application.
Each of those components can fail so it is important for us to pinpoint the right place for the issue

## Process

1. Is KafkaConnection object present?
2. What is the status of Kafka Connection object? Does all phases succeded: 

```yaml
 status:
   conditions:
   - lastTransitionTime: "2021-03-04T00:41:25.745120Z"
     message: ""
     reason: ""
     status: "True"
     type: AcccesTokenSecretValid
  - lastTransitionTime: "2021-03-04T00:41:25.745179Z"
    message: ""
    reason: ""
    status: "True"
    type: Finished
  - lastTransitionTime: "2021-03-04T00:41:25.745209Z"
    message: ""
    reason: ""
    status: "True"
    type: UserKafkasUpToDate
```

If resource was created without errors `Finished` status should be `True`

```yaml
    status: "True"
    type: Finished
```

3. Do you have Service Binding Object created?
4. Is service binding status object finished with success?


```yaml
   status: 'True'
   type: Ready
```

5. Go to your pod terminal and execute `ls /Bindings/`. Does terminal returns your service binding files?
Navigate into directory and inspect if files are present. Do you see files like `type`, `provider` etc.?
7. Go to your Application logs and inspect if there is established connection with the Kafka (or other service)
Does your application properly reads filesystem values/environment values that are present?

More info about individual resources created by RHOAS operator can be found here:
https://github.com/redhat-developer/app-services-operator/blob/main/docs/custom_resources.adoc
