/**
 * Token_server.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package DefaultNamespace;

public interface Token_server extends java.rmi.Remote {
    public java.lang.String getToken(int id) throws java.rmi.RemoteException;
    public java.lang.String generateToken(java.lang.String name, java.lang.String surname, java.lang.String JMB) throws java.rmi.RemoteException;
    public java.lang.String[] getAllTokens() throws java.rmi.RemoteException;
    public boolean verifyToken(java.lang.String tokenParam) throws java.rmi.RemoteException;
    public int getId(java.lang.String tokenParam) throws java.rmi.RemoteException;
}
