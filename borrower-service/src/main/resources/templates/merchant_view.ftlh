<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
body {font-family: Arial, Helvetica, sans-serif; margin-left: 25%; margin-right: 25%;}
* {box-sizing: border-box;}

input[type=number],input[type=date],input[type=text],  select, textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  margin-top: 6px;
  margin-bottom: 16px;
  resize: vertical;
}

input[type=submit] {
  background-color: #008CBA;
  color: white;
  padding: 12px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

input[type=submit]:hover {
  background-color: #008cbaa6;
}

.container {
  border-radius: 5px;
  background-color: #f2f2f2;
  padding: 20px;
}
</style>
</head>
<body>

<h3>Enter Details</h3>

<div class="container">
  <form action="/purchase/login" method="post">
    <label for="totalAmount">Item Purchased</label>
    <input type="text" name="itemPurchased">
    <label for="purchasedFrom">Purchased From</label>
    <input type="text" name="purchasedFrom">
    <label for="purchaseAmount">Purchased Amount</label>
    <input type="number" step=".01" name="purchaseAmount">
    <label for="tax">Tax</label>
    <input type="number" step=".01" name="tax">
    <label for="deiveryChargers">Delivery Charges</label>
    <input type="number" step=".01" name="deiveryChargers">
    <label for="deiveryChargers">Installation Charges</label>
    <input type="number" step=".01" name="installationCharges">
    <label for="totalCost">Total Cost</label>
    <input type="number" step=".01" name="totalCost">
    <label for="tenure">Tenure/Number of Installments</label>
    <input type="number" name="tenure">
    <label for="firstInstallment">First Installment</label>
    <input type="number" step=".01" name="firstInstallment">
    <label for="lastInstallment">Last Installment</label>
    <input type="number" step=".01" name="lastInstallment">
    <label for="firstInstallmentDate">First Installment Date</label>
    <input type="date" step=".01" name="firstInstallmentDate">
    <label for="lastInstallmentDate">Last Installment</label>
    <input type="date" step=".01" name="lastInstallmentDate">
    <label for="redirectUrl">Redirect URL</label>
    <input type="text" readonly name="redirectUrl" value="/dummy/thank-you">
    <input type="submit" value="Submit">
  </form>
</div>

</body>
</html>


