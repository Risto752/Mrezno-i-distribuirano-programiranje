/**
 * Token_serverService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package DefaultNamespace;

public interface Token_serverService extends javax.xml.rpc.Service {
    public java.lang.String getToken_serverAddress();

    public DefaultNamespace.Token_server getToken_server() throws javax.xml.rpc.ServiceException;

    public DefaultNamespace.Token_server getToken_server(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
