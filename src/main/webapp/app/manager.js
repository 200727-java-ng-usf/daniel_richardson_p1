
window.onload = function() {
  console.log("Loading functions...");
  // hide the main table on dashboard load
  hideAll();
  // building the tables with data
  getTicketData();

  // adding event listeners for side bar actions
  document.getElementById('resolveTicketNav').addEventListener('click', resolveFormView);
  document.getElementById('viewTicketsNav').addEventListener('click', viewTkts);
  // adding event listeners for forms
  document.getElementById('resolveBtn').addEventListener('click', resolveTicket);
  // $('#userTable').DataTable();
  console.log("Loaded functions!");
} //todo: sort show/hide for aesthetics
function deleteUser(){
  $("#userView").hide(500);
  $("#tktView").hide(500);
  $("#createUserForm").hide(500);
  $("#err-message").hide();
  $("#updateUserForm").hide(500);
  $("#deleteUserForm").show(500);
}
function viewTkts(){
  console.log("Viewing tickets...");
  // $.("#userView").toggle(); //this combines removing/setting hidden attributes
  // toggle would only work well here with 2 divs to show/hide. i'll be adding more
  $("#userView").hide(500);
  $("#tktView").show(500);
  $("#createUserForm").hide(500);
  $("#err-message").hide();
  $("#updateUserForm").hide(500);
  $("#deleteUserForm").hide(500);
}

function hideAll(){
  console.log("Hiding everything...");
  $("#err-message").hide();
  $("#userView").hide(500);
  $("#tktView").hide(500);
  $("#createUserForm").hide(500);
  $("#updateUserForm").hide(500);
  $("#deleteUserForm").hide(500);
}

function errorView(){
  $("#err-message").show();
  //http://www.robertprice.co.uk/robblog/using-jquery-to-scroll-to-an-element/
  //pulling window to the error to make it super obvious someone messed up
    //and by someone I mean the user. we don't make mistakes here, no sir.
  $('html, body').animate({
      scrollTop: ($('#err-message').first().offset().top)
  },200);
}

function deleteTargetUser(){
  console.log('Deleting user...');
  let aa = document.getElementById('emailDe').value;
  let userTarget = {
    email: aa
  }
  console.log(userTarget);
  let userJSON = JSON.stringify(userTarget); //jsonify
  let xhr = new XMLHttpRequest();
  xhr.open('POST', 'delete/user');
  // third parameter (default true) indicates we want to make this req async
  xhr.setRequestHeader('Content-type', 'application/json');
  xhr.send(userJSON);
  //
  xhr.onreadystatechange = function() {
    console.log("State: " + xhr.readyState);
    if (xhr.readyState == 4 && xhr.status == 204) { //201 created
        console.log("204")
        console.log("User deleted.")

        //todo: fiddle with DataTable api to update row
        //for now, just refresh the page to invoke the pageload functions
        document.getElementById('err-message').innerText = "Delete successful! Refreshing...";
        errorView();
        setTimeout(function(){
          location.reload();
        }, 3000); //3 second timer


    } else if(xhr.readyState == 4 && xhr.status != 204){
      errorView();
      let err = JSON.parse(xhr.responseText);
      document.getElementById('err-message').innerText = err.message;
      console.log("Error");
    }
  }

}


function updateUserInfo(){
  //updating based on email instead of ID
  //this is because of an edge case where if an admin made a user,
  //realized a mistake and wanted to update it (without refreshing),
  //but since IDs are not updated on the table (unless we pinged for the data)
  //then they would need to base the update on something else unique

  console.log('Updating user...');
  let aa = document.getElementById('usernameUp').value;
  let bb = document.getElementById('passwordUp').value;
  let cc = document.getElementById('firstNameUp').value;
  let dd = document.getElementById('lastNameUp').value;
  let ee = document.getElementById('emailUp').value;
  let ff = document.getElementById('roleUp').value;

  //making the object to jsonification
  let editUser = {
      username: aa,
      password: bb,
      firstName: cc,
      lastName: dd,
      email: ee,
      role: ff
  }
  console.log(editUser);
  let userJSON = JSON.stringify(editUser); //jsonify
  let xhr = new XMLHttpRequest();
  xhr.open('POST', 'update/user');
  // third parameter (default true) indicates we want to make this req async
  xhr.setRequestHeader('Content-type', 'application/json');
  xhr.send(userJSON);
  xhr.onreadystatechange = function() {
    console.log("State: " + xhr.readyState);
    if (xhr.readyState == 4 && xhr.status == 204) { //201 created
        console.log("204")
        console.log("User updated.")

        //todo: fiddle with DataTable api to update row
        //for now, just refresh the page to invoke the pageload functions
        document.getElementById('err-message').innerText = "Update successful! Refreshing...";
        errorView();
        setTimeout(function(){
          location.reload();
        }, 3000); //3 second timer


    } else if(xhr.readyState == 4 && xhr.status != 204){
      errorView();
      let err = JSON.parse(xhr.responseText);
      document.getElementById('err-message').innerText = err.message;
      console.log("Error");
    }
  }
}

function createNewUser(){
  console.log('Creating new user...');
  let aa = document.getElementById('usernameCr').value;
  let bb = document.getElementById('passwordCr').value;
  let cc = document.getElementById('firstNameCr').value;
  let dd = document.getElementById('lastNameCr').value;
  let ee = document.getElementById('emailCr').value;
  let ff = document.getElementById('roleCr').value;

  //making the object to jsonification
  let newUser = {
      username: aa,
      password: bb,
      firstName: cc,
      lastName: dd,
      email: ee,
      role: ff
  }
  console.log(newUser);
  let userJSON = JSON.stringify(newUser); //jsonify
  let xhr = new XMLHttpRequest();
  xhr.open('POST', 'users');
  // third parameter (default true) indicates we want to make this req async
  xhr.setRequestHeader('Content-type', 'application/json');
  xhr.send(userJSON);
  xhr.onreadystatechange = function() {
    console.log("State: " + xhr.readyState);
    if (xhr.readyState == 4 && xhr.status == 201) { //201 created
        console.log("201")
        console.log("User created.")

        //update the user table: https://datatables.net/reference/api/row.add()
        newUser.id="NEW"; //bug patch
        var table = $('#userTable').DataTable();
        var rowNode = table
            .row.add(newUser)
            .draw()
            .node();
        $( rowNode )
            .css( 'color', 'red' )
            .animate( { color: 'black' } );

        viewUsers();

    } else if(xhr.readyState == 4 && xhr.status != 201){
      errorView();
      let err = JSON.parse(xhr.responseText);
      document.getElementById('err-message').innerText = err.message + ": Email or Username may be taken, or form data was incorrect.";
      console.log("Error");
    }
  }
}

//table data fetching
//ers_user_id, username, password, first_name, last_name, email, user_role_id
// 7 columns
//should match the model's field names
function getUserData(){
  console.log("Getting user data...")

  let userJSON;
  let xhr = new XMLHttpRequest();
  xhr.open('GET', 'users');
  xhr.setRequestHeader('Content-type', 'application/json');
  xhr.send();
  xhr.onreadystatechange = function() {
      if (xhr.readyState == 4 && xhr.status == 200) {
        userJSON = JSON.parse(xhr.responseText);
        console.log("userJSON length: " + userJSON.length);
        console.log("user 0: "+ userJSON[0].username);
        $('#userTable').DataTable( {
          "autoWidth": false,
            data: userJSON,
            columns: [
                { data: 'id' },
                { data: 'username' },
                { data: 'password' },
                { data: 'firstName' },
                { data: 'lastName' },
                { data: 'email' },
                { data: 'role' }
            ]
        } );
        //there was a bug using bootstrap with the datatables...
        //data poured into the table fine, but it reset the width(css)
        //turns out, datatables rewrote the classname that bootstrap needed
        //so we'll patch this by forcing the table to have both classes:
          //also had to disable autowidth in the table init above
            //or maybe should've done that to begin with
        $("#userTable").addClass("table table-striped table-sm");


        //gu's code===============================
        // let table = document.getElementById("userTabTgt");
        // for(let i = 0 ; i < userJSON.length ; i++){
        //   let newRow = document.createElement("tr");
        //   newRow.innerHTML =
        //     "<td>" + userJSON[i].id + "</td>" +
        //     "<td>" + userJSON[i].firstName + "</td>" +
        //     "<td>" + userJSON[i].lastName + "</td>" +
        //     "<td>" + userJSON[i].username + "</td>" +
        //     "<td>" + userJSON[i].password + "</td>" +
        //     "<td>" + userJSON[i].email + "</td>" +
        //     "<td>" + userJSON[i].role + "</td>";
        //   table.appendChild(newRow);
          // IF STATEMENT FOR ROLE DISPLAY
      //   }
      // } else if(xhr.readyState == 4 && xhr.status != 200){
      //   document.getElementById('err-message').removeAttribute('hidden');
      //   let err = JSON.parse(xhr.responseText);
      //   document.getElementById('err-message').innerText = err.message;
      }
    }
}
//reimb_id, amount, submitted, resolved,
//description, username, resolver, reimb_type, reimb_status
//9 columns
//should match the model's field names
function getTicketData(){
  console.log("Getting ticket data...")

  let ticketJSON;
  let xhr = new XMLHttpRequest();
  xhr.open('GET', 'tickets');
  xhr.setRequestHeader('Content-type', 'application/json');
  xhr.send();
  xhr.onreadystatechange = function() {
      if (xhr.readyState == 4 && xhr.status == 200) {
        ticketJSON = JSON.parse(xhr.responseText);
        console.log("ticketsJSON length: " + ticketJSON.length);
        $('#ticketTable').DataTable( {
          "autoWidth": false,
            data: ticketJSON,
            columns: [
                { data: 'id' },
                { data: 'author' },
                { data: 'amount' },
                { data: 'submitted' },
                { data: 'resolve' },
                { data: 'description' },
                { data: 'resolver' },
                { data: 'type' },
                { data: 'status' }
            ]
        } );
        $("#ticketTable").addClass("table table-striped table-sm");
      }
    }
}


function alert1(){
  alert("Test"); //for debugging
}
//JQUERY
// $(document).ready( function () {
//     $('#userTable').DataTable();
// } );(jQuery);
