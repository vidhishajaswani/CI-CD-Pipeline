# DevOps-Project-Milestone 1
This is the submission by Team 5 of CSC 519 - DevOps class of Spring 2019 for the Milestone 1 of DevOps Project.

## Introduction

## Initial Steps
1. Git clone the mileston-1 branch

2. Run baker bake through both ansible-srv and jenkins-srv

3. Set-up Jenkins. 

## Automatic installation of Jenkins

1. Execute the ```java``` and ```jenkins``` roles. The ```jenkins_port``` may be set in vars/main.yml. However this is discouraged. Do NOT set to 8080 as this port is used by iTrust.  

2. Check the installation by logging into ```http://<ip_address_of_jenkins_srv>:<jenkins_port>/```. You must be able to see the log in page of Jenkins. 

## Running build job for checkbox.io

1. Run site.yml with build_checkbox.yml
2. Check 192.168.33.100:9999

##### To configure automatic execution of build job after a commit, a post-commit hook can be used with following content!
```
#!/bin/bash
curl http://192.168.33.100:9999/git/notifyCommit?url=https://github.com/ShivamChamoli/checkbox.io.git
# 192.168.33.100:9999 is the jenkins server address
# https://github.com/ShivamChamoli/checkbox.io.git is the forked repository
```
