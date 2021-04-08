<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
        <script src="https://plugin.argyle.io/argyle.web.v1.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/axios/0.21.1/axios.min.js" integrity="sha512-bZS47S7sPOxkjU/4Bt0zrhEtWx0y0CRkhEp8IckzK+ltifIIE9EMIMTuT/mEzoIMewUINruDBIR/jJnbguonqQ==" crossorigin="anonymous"></script>
        <script type="text/javascript">
    
    const token='${userToken}';
     axios.get("/employer/${employerID}").then(response=>{
       let dataPartners = [];
       console.log(response.data)
       dataPartners.push(response.data);
            executeArgyle(dataPartners);
    });

  function executeArgyle(dataPartners) {
    const argyle = Argyle.create({
        pluginKey: '${pluginKey}',
        apiHost: 'https://api-sandbox.argyle.io/v1',
        payDistributionItemsOnly: true,
        userToken: token!='nil'?token:'',
        dataPartners,
	      payDistributionConfig: '${argyleConfig}',
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
          console.log('User created: ', userId)
        },
        onClose: () => {
          document.getElementById("myForm").submit();
         //window.location.href = "/purchase/argyle_verify/${prospectId}"  
        },
        onTokenExpired: updateToken => {
          console.log('Token expired')
          // generate your new token here (more info: https://argyle.com/docs/api-reference/user-tokens)
          // updateToken(newToken)
        }
      })
      argyle.open()
      }
    </script>
  </head>
  <body>
    <form id="myForm" action="/purchase/argyle_verify/${prospectId}/${employerID}" method="post">
    <input type="hidden" value="${redirectUrl}" name="redirectUrl" />
        <input type="hidden" name="itemPurchased" value='${itemPurchased}'>
        <input type="hidden" name="purchasedFrom" value='${purchasedFrom}'>
        <input type="hidden" name="purchaseAmount" value='${purchaseAmount}'>
        <input type="hidden" name="tax" value='${tax}'>
        <input type="hidden" name="deiveryChargers" value='${deiveryChargers}'>
        <input type="hidden" name="installationCharges" value='${installationCharges}'>
        <input type="hidden" name="totalCost" value='${totalCost}'>
        <input type="hidden" name="firstInstallment" value='${firstInstallment}'>
        <input type="hidden" name="lastInstallment" value='${lastInstallment}'>
        <input type="hidden" name="tenure" value='${tenure}'>
        <input type="hidden" name="firstInstallmentDate" value='${firstInstallmentDate}'>
        <input type="hidden" name="lastInstallmentDate" value='${lastInstallmentDate}'>
        <input type="hidden" name="lastInstallmentDate" value='${lastInstallmentDate}'>
        <input type="hidden" name="redirectUrl" value='${redirectUrl}'>
        <input type="hidden" name="walletID" value='${walletID}'>
        <input type="hidden" name="accountID" value='${accountID}'>
    </form>
  </body>
</html>