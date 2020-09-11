
// REFERENCE SNIPPITS======
// document.getElementById('toLogin').addEventListener('click', loadLogin);

// NOTES===================
// XMR CODES
// 0 	UNSENT 	Client has been created. open() not called yet.
// 1 	OPENED 	open() has been called.
// 2 	HEADERS_RECEIVED 	send() has been called, and headers and status are available.
// 3 	LOADING 	Downloading; responseText holds partial data.
// 4 	DONE 	The operation is complete.

window.onload = function() {
  // adding event listeners when page loads
  document.getElementById('login-button').addEventListener('click', loginButton);
  // document.getElementById('login-button').addEventListener('click', alert1);
  console.log("click event added");
  //hiding error message
  document.getElementById('login-message').setAttribute('hidden', true);
  console.log("err msg hidden");
}

function alert1(){
  alert("Test");
}

function loginButton() { //sends ajax request to auth servlet, POST

    console.log('login function invoked!');
    let un = document.getElementById('login-username').value;
    let pw = document.getElementById('login-password').value;
    let rl = document.getElementById('login-role').value;
    //making the object to jsonification
    let credentials = {
        username: un,
        password: pw,
        role: rl
    }
    console.log(credentials);
    let credentialsJSON = JSON.stringify(credentials); //jsonify
    let xhr = new XMLHttpRequest();
    xhr.open('POST', 'auth');
    console.log("Sent to auth");
    // third parameter (default true) indicates we want to make this req async
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send(credentialsJSON);
    xhr.onreadystatechange = function() {
      console.log("state: " + xhr.readyState);
        if (xhr.readyState == 4 && xhr.status == 200) {
            console.log("200")
            // https://www.w3schools.com/js/js_window_location.asp
            // window.location.assign("admin.html"); //send to dashboard

        } else if(xhr.readyState == 4 && xhr.status == 401){
          document.getElementById('login-message').removeAttribute('hidden');
          let err = JSON.parse(xhr.responseText);
          document.getElementById('login-message').innerText = err.message;
          console.log("401")
        }
    }
}
