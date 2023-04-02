package DefaultNamespace;

public class Token_serverProxy implements DefaultNamespace.Token_server {
  private String _endpoint = null;
  private DefaultNamespace.Token_server token_server = null;
  
  public Token_serverProxy() {
    _initToken_serverProxy();
  }
  
  public Token_serverProxy(String endpoint) {
    _endpoint = endpoint;
    _initToken_serverProxy();
  }
  
  private void _initToken_serverProxy() {
    try {
      token_server = (new DefaultNamespace.Token_serverServiceLocator()).getToken_server();
      if (token_server != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)token_server)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)token_server)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (token_server != null)
      ((javax.xml.rpc.Stub)token_server)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public DefaultNamespace.Token_server getToken_server() {
    if (token_server == null)
      _initToken_serverProxy();
    return token_server;
  }
  
  public java.lang.String getToken(int id) throws java.rmi.RemoteException{
    if (token_server == null)
      _initToken_serverProxy();
    return token_server.getToken(id);
  }
  
  public java.lang.String generateToken(java.lang.String name, java.lang.String surname, java.lang.String JMB) throws java.rmi.RemoteException{
    if (token_server == null)
      _initToken_serverProxy();
    return token_server.generateToken(name, surname, JMB);
  }
  
  public java.lang.String[] getAllTokens() throws java.rmi.RemoteException{
    if (token_server == null)
      _initToken_serverProxy();
    return token_server.getAllTokens();
  }
  
  public boolean verifyToken(java.lang.String tokenParam) throws java.rmi.RemoteException{
    if (token_server == null)
      _initToken_serverProxy();
    return token_server.verifyToken(tokenParam);
  }
  
  public int getId(java.lang.String tokenParam) throws java.rmi.RemoteException{
    if (token_server == null)
      _initToken_serverProxy();
    return token_server.getId(tokenParam);
  }
  
  
}