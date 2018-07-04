/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.providers;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

/**
 *
 * @author simon
 */
public class JMSProvider {
    
    private static String URL;
    private static String JNDI_FACTORY;
    private static String JMS_FACTORY;
    private static String QUEUE;
    
    public JMSProvider() {
        loadConf();
    }
    
    private void loadConf() {
        Properties  configFile = new java.util.Properties();
	try {
	  configFile.load(this.getClass().getClassLoader().
	  getResourceAsStream("config.cfg"));
	}catch(Exception eta){
	    eta.printStackTrace();
	}
        URL = configFile.getProperty("JMS_URL", "t3://localhost:7001");
        JNDI_FACTORY = configFile.getProperty("JMS_JNDI_FACTORY", "weblogic.jndi.WLInitialContextFactory");
        JMS_FACTORY = configFile.getProperty("FACTORY", "jms/ConnectionFactory");
        QUEUE = configFile.getProperty("JMS_QUEUE", "jms/DeviceMessagingQueue");
    }
    
    public void sendMessage(String message, String deviceId) throws Exception, NamingException, JMSException {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
        env.put(Context.PROVIDER_URL, URL);
        InitialContext ic = new InitialContext(env);
        
        QueueConnectionFactory qconFactory = (QueueConnectionFactory) ic.lookup(JMS_FACTORY);
        QueueConnection qcon = qconFactory.createQueueConnection();
        QueueSession qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = (Queue) ic.lookup(QUEUE);
        QueueSender qsender = qsession.createSender(queue);
        TextMessage msg = qsession.createTextMessage();
        qcon.start();
        
        Gson gson = new Gson();
        Map<String, String> map = new HashMap();
        map.put("deviceId", deviceId);
        map.put("message", message);
        
        msg.setText(gson.toJson(map));
        qsender.send(msg);
        
        qsender.close();
    }
    
}
