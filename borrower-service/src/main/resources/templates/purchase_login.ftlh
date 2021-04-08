<!doctype html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="/docs/4.0/assets/img/favicons/favicon.ico">

    <title>Signin Template for Bootstrap</title>

    <link rel="canonical" href="https://getbootstrap.com/docs/4.0/examples/sign-in/">

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">

</head>

<body class="text-center">
    <form class="form-signin" action="/purchase/details" method="post">
        <img class="mb-4" src="http://getdrawings.com/free-icon/digital-wallet-icon-61.png" alt="" width="72"
            height="72">
        <h1 class="h3 mb-3 font-weight-normal">PayWallet - Signin</h1>
        <label for="username" class="sr-only">Username</label>
        <input type="text" id="username" name="username" class="form-control" placeholder="Username" required autofocus autocomplete="new-password">
        <label for="password" class="sr-only">Password</label>
        <input type="password" id="password" name="password" class="form-control" placeholder="Password" required autocomplete="new-password">

        <button class="btn btn-lg btn-primary btn-block" type="submit">Submit</button>
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
    </form>
</body>
<style> 
html,
body {
  height: 100%;
}

body {
    color: #000;
    overflow-x: hidden;
    height: 100%;
    background-color: #0062cc26;
  display: -ms-flexbox;
  display: -webkit-box;
  display: flex;
  -ms-flex-align: center;
  -ms-flex-pack: center;
  -webkit-box-align: center;
  align-items: center;
  -webkit-box-pack: center;
  justify-content: center;
  padding-top: 40px;
  padding-bottom: 40px;
}

.form-signin {
  width: 100%;
  max-width: 330px;
  padding: 15px;
  margin: 0 auto;
}
.form-signin .checkbox {
  font-weight: 400;
}
.form-signin .form-control {
  position: relative;
  box-sizing: border-box;
  height: auto;
  padding: 10px;
  font-size: 16px;
}
.form-signin .form-control:focus {
  z-index: 2;
}
.form-signin input[type="email"] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}
.form-signin input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}
</style>
</html>