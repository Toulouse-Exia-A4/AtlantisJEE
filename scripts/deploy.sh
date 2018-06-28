#!/bin/bash
<<<<<<< HEAD
sshpass -p '$SSH_PASS' ssh -o "StrictHostKeyChecking no" $SSH_USER@$SSH_ENDPOINT -p $SSH_PORT pkill java
=======
sshpass -p '$SSH_PASS' ssh $SSH_USER@$SSH_ENDPOINT -p $SSH_PORT touch hey
>>>>>>> 2710fb738b656297f3be015db86f4469c7dff495
