<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Purchase Details</title>

<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
  </head>
  <style> 
.no-border {
   border-width:0px;
border:none;
}

body {
      color: #000;
    overflow-x: hidden;
    height: 100%;
    background-color: #0062cc26;
}
  </style>

  <body>

    <div class="container">
      <div class="py-5 text-center">
        <img class="d-block mx-auto mb-4" src="http://getdrawings.com/free-icon/digital-wallet-icon-61.png" alt="" width="72" height="72">
        <h2>Purchase Details</h2>
      </div>
<form action="/purchase/argyle-distribution/${customerId}" method="post">
      <div class="row">
        <div class="col-md-4 order-md-2 mb-4">
          <h4 class="d-flex justify-content-between align-items-center mb-3">
            <#--  <span class="text-muted">Your cart</span>  -->
            <#--  <span class="badge badge-secondary badge-pill">3</span>  -->
          </h4>
          <ul class="list-group mb-3">
            <li class="list-group-item d-flex justify-content-between lh-condensed">
              <div>
                <small class="text-muted"> Purchase Amount </small>
              </div>
              <input class="text-muted no-border" name="purchaseAmount" value ='$${purchaseAmount}' readonly>
            </li>
            <li class="list-group-item d-flex justify-content-between lh-condensed">
              <div>
                <small class="text-muted"> Tax </small>
              </div>
              <input class="text-muted no-border" name="tax" value ='$${tax}' readonly>
            </li>
            <li class="list-group-item d-flex justify-content-between lh-condensed">
              <div>
                <small class="text-muted"> Delivery Charges </small>
              </div>
              <input class="text-muted no-border" name="deiveryChargers" value ='$${deiveryChargers}' readonly>
            </li>
            <li class="list-group-item d-flex justify-content-between lh-condensed">
              <div>
                <small class="text-muted"> Installation Charges </small>
              </div>
              <input class="text-muted no-border" name="installationCharges" value ='$${installationCharges}' readonly>
            </li>
            <li class="list-group-item d-flex justify-content-between lh-condensed">
              <div>
                <small class="text-muted"> Total Cost </small>
              </div>
              <input class="text-muted no-border" name="totalCost" value ='$${totalCost}' readonly>
            </li>
          </ul>
        </div>
        <div class="col-md-8 order-md-1">
          <#--  <h4 class="mb-3">Billing address</h4>  -->
          <#--  <form class="needs-validation" novalidate>  -->
            <div class="row">
              <div class="col-md-6 mb-3">
                <label for="itemPurchased">Item Purchased</label>
                <input type="text" class="form-control" name="itemPurchased" placeholder="" value="${itemPurchased}" readonly>
              </div>
              <div class="col-md-6 mb-3">
                <label for="lastName">Purchased From</label>
                <input type="text" class="form-control" name="purchasedFrom" placeholder="" value="${purchasedFrom}" readonly>
              </div>
              <div class="col-md-6 mb-3">
                <label for="lastName">First Installment</label>
                <input type="text" class="form-control" name="firstInstallment" placeholder="" value="${firstInstallment}" readonly>
              </div>
              <div class="col-md-6 mb-3">
                <label for="lastName">Last Installment</label>
                <input type="text" class="form-control" name="lastInstallment" placeholder="" value="${lastInstallment}" readonly>
              </div>
              <div class="col-md-6 mb-3">
                <label for="lastName">First Installment Date</label>
                <input type="text" class="form-control" name="firstInstallmentDate" placeholder="" value="${firstInstallmentDate}" readonly>
              </div>
              <div class="col-md-6 mb-3">
                <label for="lastName">Last Installment Date</label>
                <input type="text" class="form-control" name="lastInstallmentDate" placeholder="" value="${lastInstallmentDate}" readonly>
              </div>
              <div class="col-md-6 mb-3">
                <label for="lastName">Tenure</label>
                <input type="text" class="form-control" name="tenure" placeholder="" value="${tenure}" readonly>
              </div>
               <div class="col-md-6 mb-3">
                <label for="employer">Select Employer</label>
                <select class="custom-select d-block w-100" id="selectBox" name="employer">
                </select>
              </div>
            </div>
               <input type="hidden" name="redirectUrl" value='${redirectUrl}' readonly>

            <hr class="mb-4">
            <button class="btn btn-primary btn-lg btn-block" type="submit">I agree, Continue</button>
        </div>
          </form>
      </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
      <script type="text/javascript">
      var employers = '${employers}';
      console.log(employers);
      var options = employers.substring(1, employers.length - 1).split(","), selectHtml = "";
      console.log(options);
      for (var optionIndex = 0; optionIndex < options.length; optionIndex++) {
         selectHtml += ("<option>" + options[optionIndex] + "</option>");
      }
      document.getElementById("selectBox").innerHTML = selectHtml;
    </script>
  </body>
</html>