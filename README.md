# DevOps-Project-Milestone 2

This is the submission by Team 5 of CSC 519 - DevOps class of Spring 2019 for the Milestone 2 of DevOps Project. The contributors in alphabetic order are:

Karthik Medidisiva  -   kmedidi

Kshittiz Kumar      -   kkumar4

Shivam Chamoli      -   schamoli

Vidhisha Jaswani    -   vjaswan

## Introduction
In this milestone the following objectives were tackled.

 Techniques related to fuzzing, test case priorization, and static analysis to improve the quality of checkbox.io and iTrust.

## Roles & their Description

1. Ansible: Installs Ansible on Jenkins VM.
2. Maven: Installs Maven on Jenkins VM.
3. Java: Installs Java 8 on Jenkins VM.
4. MySQL: Install MySQL and edits root password on Jenkins VM.
5. Deployfiles: Copies all files to Jenkins VM required for building the applications.
6. Jenkins: Automatically installs Jenkins on ```http://<ip_address_of_jenkins_srv>:<jenkins_port>/``` along with bypassing the user account setup and installing plugins.
7. Jobs: Creates and triggers build for checkbox.io and iTrust applications.
8. jFuzzer: Java code to read all java files and fuzz the code with some probability. It resets the head after each commit so that each time we fuzz, we fuzz the base code.
9. Test-Prioritization: NodeJS code to prioritize tests.


## Workflow

1. We setup our Server by installing all dependencies such as Maven, Ansible, MySQL, Java first. We copy all our required files onto this server using the deployfiles role.
2. Jenkins is then installed on this server. We have used port 9999 for Jenkins since iTrust runs on 8080. You may access Jenkins on 192.168.33.100:9999 and login using username ```jenkins``` and password ```jenkins```. You may change these values in variables.yml.
3. After Jenkins is setup, Jobs for checkbox.io, iTrust Fuzzer, and iTrust are created. The iTrust Fuzzer job runs the command ```cd /home/vagrant/jFuzzer && sudo mvn assembly:assembly -DdescriptorId=jar-with-dependencies && sudo java -cp target/jFuzzer-0.0.1-SNAPSHOT-jar-with-dependencies.jar edu.ncsu.fuzzer.Application  ``` which runs the fuzzer code. The fuzzer code fuzzes the code with some probability, commits the code, and after a designated sleep time resets the head so that the next time we fuzz, we do it on the stable code. As soon as the commit is made from the Java code, a [file](hooks/post-commit) is invoked which is kept inside ```iTrust2-v4/iTrust2/.git/hooks```. We copy this hook automatically using deployfiles role. This hook in turn calls ```ansible-playbook /home/vagrant/job_rebuilds/rebuild_itrust_job.yml``` builds the iTrust job. 
4. When the iTrust Job is build we run the following steps:
4.1 ```ansible-playbook /var/lib/jenkins/deploy_itrust_fuzzer.yml``` . This ansible playbook does all things neccessary for iTrust deployment such as copying email.properties and db.properties.
4.2 Inside this playbook we call a nodeJS code that in turn calls the ```mvn clean install && mvn test``` commands.
4.3 After this, in build shell we call the ``` mvn checkstyle:checkstyle``` command to generate checkstyle reports.
5. You can alter the number of commits in [file](ansible-srv/roles/jFuzzer/src/main/java/edu/ncsu/fuzzer/ItrustFuzzing.java) on line 24 for variable COMMITS.
6. We have also set a threshold for our iTrust job that if code coverage (Instructions, % Branch, % Complexity, % Line, % Method, % Class) is above 25%, our job always passes and if it is below 25% our job always fails. So when the iTrust Job is built, it may pass or fail according to this threshold.
7. After the build is complete, you can see the Jacoco reports from the UI itself regardless if the build fails or passes.


## About the Fuzzer
For this milestone, we designed a tool called jFuzzer (maven project) using [JavaParser](http://javaparser.org/) API and [JGit](https://git-scm.com/book/uz/v2/Appendix-B%3A-Embedding-Git-in-your-Applications-JGit). JavaParser APIs allows us to parse java source code and build Abstract Syntax Tree (AST) which further can be used to do fuzzing.
**Fuzzing operations perforemd using jFuzzer**
- change string constant (50% probability)
  - introduce a new string constant called 'FUZZY' with probablity of 20%
  - introduce a new string constant called 'MORE_FUZZY' with probablity of 30%
- swap true with false values and vice versa (probability 50%)
- swap binary-relational operators (probability 70%)
  - swap "<" with ">" (not > with < to avoid lambda expressions errors)
  - swap "*" (multiply) with "/" (divide)
  - swap "|" (binary AND) with "&" (binary OR) and vice versa
- swap "==" with "!=" and vice versa (probablity 30%)
- swap "+=" with "=" (probability 30%)

**How does JGit helps?** JGit allows java program to execture git commands. In jFuzzer a new fuzzer branch is created if not present using JGit. Further it commits the code after fuzzing is done on the source files and reset it at the end. This is done till 100 commits are made. *For fail-safe a final reset is done to the original commit of iTrust*.
## Test Prioritization


## Instructions for execution
Follow the below instructions.

### Initial Steps

1. Git clone the master branch and set up the ansible-srv and jenkins-srv using baker bake.

2. The inventory file has the details for jenkins-srv VM. You may edit the IP address if necessary. It is assumed that the jenkins-srv runs as the host for source repository that tracks the changes made to the Enterprise applications ```checkbox.io``` and ```iTrust2```.

3. Set up the interactions between local repo and this source repo. The following instructions achieve the same. 

### Instructions for creating the interactions between Git repositories
3.1. On the jenkins-srv VM, create the production git repositories ```checkbox.git``` and ```itrust.git```.

3.2. Inside the ```*.git``` directories, run the following command to initialize as a bare repository.
<br>```$ git init --bare```

3.3. At the host machine (which is able to SSH into the jenkins-srv without the need to specify identity file), clone the checkbox.io and iTrust applications from the online github repositories ([checkbox.io](https://github.com/ShivamChamoli/checkbox.io) and [iTrust](https://github.ncsu.edu/engr-csc326-staff/iTrust2-v4)).

3.4. Now navigate inside this repo and add the bare repositories created inside jenkins-srv as a remote repo called ```prod```.
<br>```$ git remote add prod vagrant@<IP of jenkins-srv>:/~/<bare_repo>```

For example,
<br>```$ git remote add prod vagrant@192.168.33.100:/~/checkbox.git```

3.5. Create an initial push into this bare repo from the local repo
<br>```$ git push prod master```

NOTE: You may edit this [file](variables.yml) if you have the remote repo at a different location.



### Setting up Jenkins, Fuzz the code to create 100 commits, Run build jobs for iTrust

1. Execute the the command ```ansible-playbook -i inventory playbook.yml```. This will install all dependencies such as maven, ansible, mysql
   NOTE:
   The variables need to be set in variables.yml. Do NOT set to 8080 as this port is used by  iTrust. We have used port 9999. With our dummy values, a new user with the credentials as follows is created. username: ```jenkins``` password: ```jenkins```. You may change these credentials in variables.yml for this role.

2. Check the installation by logging into ```http://<ip_address_of_jenkins_srv>:<jenkins_port>/```, in our case 192.168.33.100:9999. You must be able to see the log in page of Jenkins.



## Results


![coveragetrends](results/coveragetrends.png)
![thresholds](results/thresholds.png)
![pass_coverage](results/pass_coverage.png)
![failure_coverage](results/failure_coverage.png)


## Contribution
1. **Karthik Medidisiva** : 

2. **Kshittiz Kumar**: 

3. **Shivam Chamoli**: 

4. **Vidhisha Jaswani**: 
 
## Screencast
[Screencast Link]()

**Thank you!**
