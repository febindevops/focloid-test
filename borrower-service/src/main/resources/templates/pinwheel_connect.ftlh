<!DOCTYPE html>
<html lang="en">

<head>
  <title> Pinwheel Connect </title>
  <script src="https://cdn.getpinwheel.com/pinwheel-v2.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/axios/0.21.1/axios.min.js"
    integrity="sha512-bZS47S7sPOxkjU/4Bt0zrhEtWx0y0CRkhEp8IckzK+ltifIIE9EMIMTuT/mEzoIMewUINruDBIR/jJnbguonqQ=="
    crossorigin="anonymous"></script>
  <script>
    Pinwheel.open({
      linkToken: '${token}',
      onSuccess(result) {
        console.log(result);
        if (result && result.accountId) {
          axios.put("/borrower/pinwheel/${pinWheelId}/" + result.accountId).then(response => {
            let data = response.data;
            if (data && data.pinwheelPrimaryId) {
              alert('Updated successfully with Account ID ' + result.accountId);
            }
          });
        }
      },
      onLogin(result) {
        console.log(result);
      },
      onError(error) {
        console.log(error);
      },
      onExit(error) {
        console.log(error);
      },
      onEvent(eventName, payload) {
        console.log(eventName, payload);
      }
    });
  </script>
</head>

<body></body>

</html>