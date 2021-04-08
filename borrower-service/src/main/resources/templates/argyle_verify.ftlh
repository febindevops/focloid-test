<!DOCTYPE html>
<html lang="en">

<head>
   <title>Argyle Verify</title>
   <meta charset="utf-8" />
   <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
   <script src="https://cdnjs.cloudflare.com/ajax/libs/axios/0.21.1/axios.min.js"
      integrity="sha512-bZS47S7sPOxkjU/4Bt0zrhEtWx0y0CRkhEp8IckzK+ltifIIE9EMIMTuT/mEzoIMewUINruDBIR/jJnbguonqQ=="
      crossorigin="anonymous"></script>
   <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
   <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
   <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
   <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
   <script src='https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.bundle.min.js'></script>
   <link href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css'>
</head>

<body>
   <div class="otp bs-example" id="otpScreen">
      <label for="formGroupExampleInput">Please verify your identity with the otp sent to you registred mobile number: </label>

      <div class="form-group row">
         <label for="otp" class="col-sm-2 col-form-label">OTP</label>
         <div class="col-sm-10">
            <input type="text" class="form-control" id="otpInput" placeholder="4 Digit OTP">
         </div>
      </div>
      <div class="form-group row">
         <div class="col-sm-10 offset-sm-2">
            <button type="submit" class="btn btn-primary" id="otpSubmit">Verify</button>
         </div>
      </div>

      </form>
   </div>
   <div id="preloader">
      <div class="loading">Loading&#8230;</div>
   </div>

   <div class="container-fluid px-1 px-md-2 px-lg-4 py-5 mx-auto" id="summary">
      <div class="row d-flex justify-content-center">
         <div class="col-xl-6 col-lg-6 col-md-10 col-sm-12">
            <div class="card border-0">
               <div class="text-center">
                  <img class="mb-4"
                     src="http://getdrawings.com/free-icon/digital-wallet-icon-61.png"
                     alt="" width="72" height="72">
                  <h1 class="h3 mb-3 font-weight-normal">Purchase Details</h1>
               </div>
               <div class="row">
                  <div class="col-sm-7 border-line pb-3">
                     <div class="form-group">
                        <p class="text-muted text-sm mb-0">Item purchased</p> <input style='width: 100%;' type="text"
                           name="itemPurchased" value='${itemPurchased}' placeholder="Name" size="15" readonly>
                     </div>
                     <div class="form-group">
                        <p class="text-muted text-sm mb-0">Purchased From</p> <input style='width: 100%;' type="text"
                           name="purchasedFrom" value='${purchasedFrom}' placeholder="Name" size="15" readonly>
                     </div>
                     <div class="form-group">
                        <p class="text-muted text-sm mb-0">Wallet ID</p> <input style='width: 100%;' type="text"
                           name="walletID" value='${walletID}' placeholder="Name" size="15" readonly>
                     </div>
                     <div class="form-group">
                        <p class="text-muted text-sm mb-0">Account ID</p> <input style='width: 100%;' type="text"
                           name="accountID" value='${accountID}' placeholder="Name" size="15" readonly>
                     </div>
                     <div class="form-group">
                        <p class="text-muted text-sm mb-0">First Installment</p> <input style='width: 100%;' type="text"
                           name="firstInstallment" value='${firstInstallment}' placeholder="Name" size="15" readonly>
                     </div>
                     <div class="form-group">
                        <p class="text-muted text-sm mb-0">Last Installment</p> <input style='width: 100%;' type="text"
                           name="lastInstallment" value='${lastInstallment}' placeholder="Name" size="15" readonly>
                     </div>
                     <div class="form-group">
                        <p class="text-muted text-sm mb-0">First Installment Date</p> <input style='width: 100%;'
                           type="text" name="firstInstallmentDate" value='${firstInstallmentDate}' placeholder="Name"
                           size="15" readonly>
                     </div>
                     <div class="form-group">
                        <p class="text-muted text-sm mb-0">Last Installment Date</p> <input style='width: 100%;'
                           type="text" name="lastInstallmentDate" value='${lastInstallmentDate}' placeholder="Name"
                           size="15" readonly>
                     </div>
                     <div class="form-group">
                        <p class="text-muted text-sm mb-0">Tenure</p> <input style='width: 100%;' type="text"
                           name="tenure" value='${tenure}' placeholder="Name" size="15" readonly>
                     </div>
                  </div>
                  <div class="col-sm-5 text-sm-center justify-content-center pt-4 pb-4">
                     <small class="text-sm text-muted">Purchase Amount</small>
                     <h5 class="mb-5">${purchaseAmount}</h5>
                     <small class="text-sm text-muted">Tax</small>
                     <h5 class="mb-5">${tax}</h5>

                     <small class="text-sm text-muted">Delivery Charges</small>
                     <h5 class="mb-5">${deiveryChargers}</h5>

                     <small class="text-sm text-muted">Installation Charges</small>
                     <h5 class="mb-5">${installationCharges}</h5>

                     <small class="text-sm text-muted">Total Cost</small>
                     <div class="row px-3 justify-content-sm-center">
                        <h2 class=""><span class="text-md font-weight-bold mr-2"></span><span
                              class="text-primary">${totalCost}</span></h2>
                     </div> <button type="submit" id='submitBtn' class="btn btn-blue text-center mt-4">SUBMIT</button>
                  </div>
               </div>
            </div>
         </div>
      </div>
   </div>
   </div>
   </div>
   <script type="text/javascript">

      var checkInterval = null;
      $(function () {
         $("#preloader").hide();
         $("#summary").hide();
         $("#otpSubmit").click(function (e) {
            if ($("#otpInput").val() != "1234") {
               alert('Invalid OTP');
               return;
            }
            e.preventDefault();
            if (!checkInterval) {
               checkInterval = setInterval(callArgyleAccounts, 5000);
               $("#preloader").show();
               $("#otpScreen").hide();
            }
         });
      });

      function callArgyleAccounts() {
         axios.get("/borrower/argyle-accounts/${customerId}/${employerId}").then(response => {
            let data = response.data;
            console.log(data);
            if (data && data.payDistributionStatus === 'success') {
               myStopFunction();
            } else if (data && data.status === 'done') {
               alert('An unexpected error occured! Argyle Payroll Allocation Failed.');
               window.location.href = "${redirectUrl}";
            }
         });
      }

      function myStopFunction() {
         clearInterval(checkInterval);
         $("#preloader").hide();
         $("#summary").show();
      }

      $("#submitBtn").click(function (e) {
         window.location.href = "${redirectUrl}";
      });

   </script>
   <style>
      .otp {
         margin-top: 15%;
         margin-left: 25%;
         margin-right: 25%;
      }

      body {
         color: #000;
         overflow-x: hidden;
         height: 100%;
         background-color: #0062cc26;
         background-repeat: no-repeat
      }

      .card {
         padding: 30px 25px 35px 50px;
         border-radius: 10px;
         box-shadow: 0px 4px 8px 0px #0062cc;
      }

      .border-line {
         border-right: 1px solid #BDBDBD
      }

      .text-sm {
         font-size: 13px
      }

      .text-md {
         font-size: 18px
      }

      .image {
         width: 60px;
         height: 30px
      }

      ::placeholder {
         color: grey;
         opacity: 1
      }

      :-ms-input-placeholder {
         color: grey
      }

      ::-ms-input-placeholder {
         color: grey
      }

      input {
         padding: 2px 0px;
         border: none;
         border-bottom: 1px solid lightgrey;
         margin-bottom: 5px;
         margin-top: 2px;
         box-sizing: border-box;
         color: #000;
         font-size: 16px;
         letter-spacing: 1px;
         font-weight: 500
      }

      input:focus {
         -moz-box-shadow: none !important;
         -webkit-box-shadow: none !important;
         box-shadow: none !important;
         border-bottom: 1px solid #117a8b;
         outline-width: 0
      }

      button:focus {
         -moz-box-shadow: none !important;
         -webkit-box-shadow: none !important;
         box-shadow: none !important;
         outline-width: 0
      }

      .btn-blue {
         background-color: #0062ccfa;
         color: #fff;
         padding: 8px 25px;
         border-radius: 20px;
         font-size: 18px;
         letter-spacing: 2px;
         border: 2px solid #fff
      }

      .btn-blue:hover {
         box-shadow: 0 0 0 2px #117a8b
      }

      .btn-blue:focus {
         box-shadow: 0 0 0 2px #117a8b !important
      }

      .custom-checkbox .custom-control-input:checked~.custom-control-label::before {
         background-color: #117a8b
      }

      @media screen and (max-width: 575px) {
         .border-line {
            border-right: none;
            border-bottom: 1px solid #EEEEEE
         }
      }

      #preloader {
         position: fixed;
         top: 0;
         left: 0;
         width: 100%;
         height: 100%;
      }

/* Absolute Center Spinner */
.loading {
  position: fixed;
  z-index: 999;
  overflow: show;
  margin: auto;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  width: 50px;
  height: 50px;
}

/* Transparent Overlay */
.loading:before {
  content: '';
  display: block;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(255,255,255,0.5);
}

/* :not(:required) hides these rules from IE9 and below */
.loading:not(:required) {
  /* hide "loading..." text */
  font: 0/0 a;
  color: transparent;
  text-shadow: none;
  background-color: transparent;
  border: 0;
}

.loading:not(:required):after {
  content: '';
  display: block;
  font-size: 10px;
  width: 50px;
  height: 50px;
  margin-top: -0.5em;

  border: 15px solid rgba(33, 150, 243, 1.0);
  border-radius: 100%;
  border-bottom-color: transparent;
  -webkit-animation: spinner 1s linear 0s infinite;
  animation: spinner 1s linear 0s infinite;


}

/* Animation */

@-webkit-keyframes spinner {
  0% {
    -webkit-transform: rotate(0deg);
    -moz-transform: rotate(0deg);
    -ms-transform: rotate(0deg);
    -o-transform: rotate(0deg);
    transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
    -moz-transform: rotate(360deg);
    -ms-transform: rotate(360deg);
    -o-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
@-moz-keyframes spinner {
  0% {
    -webkit-transform: rotate(0deg);
    -moz-transform: rotate(0deg);
    -ms-transform: rotate(0deg);
    -o-transform: rotate(0deg);
    transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
    -moz-transform: rotate(360deg);
    -ms-transform: rotate(360deg);
    -o-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
@-o-keyframes spinner {
  0% {
    -webkit-transform: rotate(0deg);
    -moz-transform: rotate(0deg);
    -ms-transform: rotate(0deg);
    -o-transform: rotate(0deg);
    transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
    -moz-transform: rotate(360deg);
    -ms-transform: rotate(360deg);
    -o-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
@keyframes spinner {
  0% {
    -webkit-transform: rotate(0deg);
    -moz-transform: rotate(0deg);
    -ms-transform: rotate(0deg);
    -o-transform: rotate(0deg);
    transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
    -moz-transform: rotate(360deg);
    -ms-transform: rotate(360deg);
    -o-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
   </style>
</body>

</html>