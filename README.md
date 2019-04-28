# DevOps-Project-Milestone 3

This is the submission by Team 5 of CSC 519 - DevOps class of Spring 2019 for the Milestone 3 of DevOps Project. The contributors in alphabetic order are:

Karthik Medidisiva  -   kmedidi

Kshittiz Kumar      -   kkumar4

Shivam Chamoli      -   schamoli

Vidhisha Jaswani    -   vjaswan

## Introduction
Use milestone_3 branch for this submission
In this milestone the following objectives were tackled.


## Replication Instructions

Fill in all credentials include AWS Secret Access Key and Access key in ansible-srv/variables.yml

1. Git clone the milestone_3 branch and set up the ansible-srv using ```baker bake```.
2. SSH into the ansible-srv using ```baker ssh```.
3. Navigate to the shared folder inside this VM i.e ```ansible-srv/```.
4. Run the playbook to setup the Jenkins EC2 Instance

```ansible-playbook -i inventory setup_jenkins.yml```

5. Run the playbook to deploy jenkins and setup other configuration parameters

```ansible-playbook -i inventory setup_production.yml```

6. Go inside the ~/.ssh of your local computer and create a key-pair using ```ssh-keygen -t rsa -b 4096 -C “ec2jenkins” -f ec2jenkins```.  Copy the content of ec2jenkins.pub. Now SSH into the Jenkins machine using command  ```ssh -i "jenkins.pem" ubuntu@<ip>```.  You can pick this IP from your Amazon IAM Console. Inside this jenkins machine go to the ~/.ssh folder and paste contents of public key in authorized_keys file. Check if you are able to SSH into the jenkins machine via your local machine.

7. On your local machine clone the checkbox repo [checkbox](https://github.com/ShivamChamoli/checkbox.io) and navigate into this folder and set up the prod remote as 


```$ git remote add prod vagrant@<IP of jenkins-srv>:/~/checkbox.io.git```

Make sure you did not have an existing remote prod.

Now make a commit and push to bare repository in order to have the code there 
```
touch abcd
git add .
git commit -m "first"
git push prod master
```

Make sure you see code inside checkbox.io folder kept on home of Jenkins EC2.

8. On your local machine clone the iTrust repo [iTrust](https://github.ncsu.edu/schamol/iTrust2-v4.git) and navigate into this folder and set up the prod remote as 


```$ git remote add prod vagrant@<IP of jenkins-srv>:/~/iTrust2-v4.git```

Make sure you did not have an existing remote prod.

Now make a commit and push to bare repository in order to have the code there 
```
touch abcd
git add .
git commit -m "first"
git push prod master
```

Make sure you see code inside iTrust2-v4 folder kept on home of Jenkins EC2.

9.  Now from the ansible-srv machine run command 

```
ansible-playbook -i inventory jobs.yml
```



## Deployment on EC2

1. We setup our Jenkins Server by installing all dependencies such as Maven, Ansible, MySQL, Java first. 
2. Jenkins is then installed on this server. We have used port 9999 for Jenkins since iTrust runs on 8080. You may access Jenkins and login using username ```jenkins``` and password ```jenkins```. You may change these values in variables.yml.
3. After Jenkins is setup, a new instance spins up and installs all dependencies such as nginx, mysql, node, java, mongodb. Then jobs for checkbox.io, iTrust Fuzzer,  iTrust, checkbox analysis are created. As soon as the commit is made from the checkbox code, a [file](ansible-srv/roles/create-jobs/checkbox_post_receive.j2) is invoked which is inside ```checkbox.io.git/hooks``` and for iTrust, [file](ansible-srv/roles/create-jobs/itrust_post_receive.j2 )which is kept inside ```iTrust2-v4.git/hooks```. This hook invokes the build job.
4.  When the build jobs are invoked each application is then deployed on AWS EC2 instances.



## Feature Flags

Feature Toggles (often also refered to as Feature Flags) are a powerful technique, allowing teams to modify system behavior without changing code. In our implementation of feature toggles, we have used redis/redis-server (an in-memory data structure project implementing a distributed, in-memory key-value database with optional durability) as our configuration server which initially has these flags set to nil :
 1. **featureOffFlagManageHospitals** - flag when 'TRUE' can turn off manage hospitals functionality and redirect to admin homepage.
 2. **featureOffFlagManageUsers** - flag when 'TRUE' can turn off manage users functionality and redirect to admin homepage.
 3. **featureOffFlagManageDrugs** - flag when 'TRUE' can turn off manage drugs functionality and redirect to admin homepage.
 4. **featureOffFlagManageICDCodes** - flag when 'TRUE' can turn off manage ICDCodes functionality and redirect to admin homepage.
 
To toggle the value of these flags we use the redis-cli. We also made changes to our fork of [iTrust](https://github.ncsu.edu/schamol/iTrust2-v4) in order to incorporate the changes for toggling features. We added a utility class in itrust fork called JedisUtil (uses Redis java client api) to communicate with the redis-server in order to get/set the values of the featureOffFlag. Following is the simple code snippet of ManageHospitals feature flag.
```
public String manageHospital ( final Model model ) {
        if(!JedisUtil.getFeatureOff("ManageHospitals"))
        {
            return "/admin/hospitals";
        } else return "/admin/index";
    }
```

## Infrastructure and Microservice

## Something Special

For our special milestone we are doing Monitoring of our production environment (iTrust + Checkbox.io). We have done monitoring through Prometheus (open-source software project to record real-time metrics in a time series database built using a HTTP pull model). To verify whether Prometheus was monitoring the right instance, we stressed the instance using:
```
stress --cpu 8 --io 4 --vm 1 --vm-bytes 1024M --timeout 15s
```
And saw a spike and a trough after 15s (which is exactly what the command does). We also tried to include Alerting to a Slack channel when node load (cpu load) is > 0.5 (alert rule shown below) and were able to post to the channel through postman (as shown in the figure below) successfully but faced some configuration issues and have moved it to future scope.
```
- name: alert.rules
  rules:
  - alert: high_cpu_load
    expr: node_load1 > 0.5
    for: 1s
    labels:
      severity: warning
    annotations:
      summary: "Server under high load"
      description: EC2 instance is under high load, the avg load 1m is at {{ $value}}. Reported by instance {{ $labels.instance }} of job {{ $labels.job }}."
```

## Other Features 

1. We have also integrated the features such as fuzzing of iTrust code as a separate workflow. When a developer commits on his local machine, Jenkins takes the code from the repository, fuzzes the code, builds the ITrust fuzzer job and deploys the code on the production environment. This happens 100 times (same as milestone 2). We also did test case prioritization which ranks the order in which test cases should be run based on how many times it failed and the total time it took to run it when it failed.

2. We also integrated the custom analysis of checkbox where it analysis  
## Results




## Contribution
1. **Karthik Medidisiva** :

2. **Kshittiz Kumar**: 

3. **Shivam Chamoli**: 

4. **Vidhisha Jaswani**: 
 
## Screencast
[Screencast Link](https://youtu.be/xwh4ocSP8po)

**Thank you!**
