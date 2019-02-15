## DevOps-Project

###To run a basic jenkins jobs
1. Download the mileston-1 branch
2. Run baker bake through both ansible-srv and web-srv
3. [Setup jenkins](https://linuxize.com/post/how-to-install-jenkins-on-ubuntu-18-04/)
4. Setup your admin account on jenkins
5. Go to http://192.168.33.100/me/configure and generate a token
6. Add that token to roles/templates/jenkins_jobs.ini.j2