/**
 * Token_serverServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package DefaultNamespace;

public class Token_serverServiceLocator extends org.apache.axis.client.Service implements DefaultNamespace.Token_serverService {

    public Token_serverServiceLocator() {
    }


    public Token_serverServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public Token_serverServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for Token_server
    private java.lang.String Token_server_address = "http://localhost:8080/TokenServer/services/Token_server";

    public java.lang.String getToken_serverAddress() {
        return Token_server_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String Token_serverWSDDServiceName = "Token_server";

    public java.lang.String getToken_serverWSDDServiceName() {
        return Token_serverWSDDServiceName;
    }

    public void setToken_serverWSDDServiceName(java.lang.String name) {
        Token_serverWSDDServiceName = name;
    }

    public DefaultNamespace.Token_server getToken_server() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Token_server_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getToken_server(endpoint);
    }

    public DefaultNamespace.Token_server getToken_server(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            DefaultNamespace.Token_serverSoapBindingStub _stub = new DefaultNamespace.Token_serverSoapBindingStub(portAddress, this);
            _stub.setPortName(getToken_serverWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setToken_serverEndpointAddress(java.lang.String address) {
        Token_server_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (DefaultNamespace.Token_server.class.isAssignableFrom(serviceEndpointInterface)) {
                DefaultNamespace.Token_serverSoapBindingStub _stub = new DefaultNamespace.Token_serverSoapBindingStub(new java.net.URL(Token_server_address), this);
                _stub.setPortName(getToken_serverWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("Token_server".equals(inputPortName)) {
            return getToken_server();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://DefaultNamespace", "Token_serverService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://DefaultNamespace", "Token_server"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("Token_server".equals(portName)) {
            setToken_serverEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
