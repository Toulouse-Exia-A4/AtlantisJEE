echo $SSH_PASS
echo $SSH_USER
echo $SSH_ENDPOINT
echo $SSH_PORT
sshpass -p '$SSH_PASS' ssh $SSH_USER@$SSH_ENDPOINT -p $SSH_PORT "touch hey"
