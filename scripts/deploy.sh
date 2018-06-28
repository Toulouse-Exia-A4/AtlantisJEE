echo $SSH_PASS
echo $SSH_USER
echo $SSH_ENDPOINT
echo $SSH_PORT
sshpass -p '$SSH_PASS' ssh $SSH_USER@$SSH_ENDPOINT -p $SSH_PORT "touch hey"
echo hi
sshpass -p '$SSH_PASS' ssh $SSH_USER@$SSH_ENDPOINT -p $SSH_PORT "$DEPLOY_PATH/bin/asadmin undeploy AtlantisJEE"
echo hi
sshpass -p '$SSH_PASS' scp -P $SSH_PORT target/AtlantisJEE.war $SSH_USER@$SSH_ENDPOINT:$DEPLOY_PATH
echo hi
sshpass -p '$SSH_PASS' ssh $SSH_USER@$SSH_ENDPOINT -p $SSH_PORT "$DEPLOY_PATH/bin/asadmin deploy $DEPLOY_PATH/AtlantisJEE.war"
echo hi
