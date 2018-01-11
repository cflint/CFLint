component{
  function foo(){
    application.facebookSDK.facebookApp = new facebook.sdk.FacebookApp(
        appId=application.config.FBPhotoEndpoint_APPID, 
        secretKey=application.config.FBPhotoEndpoint_SECRET_KEY, 
        apiVersion=application.config.FBPhotoEndpoint_Version);
  } 
}