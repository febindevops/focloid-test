<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
  </head>
  <body>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/axios/0.21.1/axios.min.js" integrity="sha512-bZS47S7sPOxkjU/4Bt0zrhEtWx0y0CRkhEp8IckzK+ltifIIE9EMIMTuT/mEzoIMewUINruDBIR/jJnbguonqQ==" crossorigin="anonymous"></script>
    <script src="https://plugin.argyle.io/argyle.web.v1.js"></script>
    <script type="text/javascript">

    axios.get("/borrower/employers/${prospectId}").then(response=>{
            let employers = response.data.filter(data=>data.argyleId!==null).map(data=>data.argyleId);
            executeArgyle(employers);
    });
    var connected = false;
   function executeArgyle(dataPartners){
      if(!dataPartners || dataPartners.length == 0){
        alert('No employers attached to the prospect !');
        return;
      }
      const token = '${userToken}';
      const argyle = Argyle.create({
        pluginKey: '${pluginKey}',
        apiHost: 'https://api-sandbox.argyle.io/v1',
         userToken: token!='nil'?token:'',
         payDistributionItemsOnly: true,
         dataPartners,
        onAccountCreated: ({ accountId, userId }) => {
          console.log('Account created: ', accountId, ' User ID:', userId)
        },
        onAccountConnected: ({ accountId, userId }) => {
          console.log('Account connected: ', accountId, ' User ID:', userId)
        },
        onAccountUpdated: ({ accountId, userId }) => {
          console.log('Account updated: ', accountId, ' User ID:', userId)
        },
        onAccountRemoved: ({ accountId, userId }) => {
          console.log('Account removed: ', accountId, ' User ID:', userId)
        },
        onUserCreated: ({ userId, userToken }) => {
          console.log('User created: ', userId);
          console.log('User Token: ', userToken);
          axios.post("/borrower/argyle-connect/${prospectId}",{
            userId,
            userToken
          }).then(response=>{
            connected = true;
            console.log(response);
          });
          
        },
        onClose: () => {
          console.log('Link closed');
         // window.parent.document.dispatchEvent(new CustomEvent('aggregatorCloseEvent', {prospectId:${prospectId},connected}))
          window.parent.postMessage({ message: "aggregatorCloseEvent", data:{prospectId:${prospectId},connected} }, "*");

        },
        onTokenExpired: updateToken => {
          console.log(updateToken);
          console.log('Token expired')
          // generate your new token here (more info: https://argyle.com/docs/api-reference/user-tokens)
          // updateToken(newToken)
        }
      })
      argyle.open();
   }

    </script>
  </body>
</html>