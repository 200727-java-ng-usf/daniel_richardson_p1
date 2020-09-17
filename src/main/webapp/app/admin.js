
window.onload = function() {
  console.log("Loading functions...");
  // hide the main table on dashboard load
  hideAll();
  viewUsers(); //just auto show users
  // building the tables with data
  getUserData();
  getTicketData();

  // adding event listeners for side bar actions
  // document.getElementById('home').addEventListener('click', hideAll);
  document.getElementById('viewUsers').addEventListener('click', viewUsers);
  document.getElementById('createUser').addEventListener('click', createUser);
  document.getElementById('updateUser').addEventListener('click', updateUser);
  // document.getElementById('deleteUser').addEventListener('click', deleteUser);
  document.getElementById('viewTickets').addEventListener('click', viewTkts);
  document.getElementById('logoutNav').addEventListener('click', logout);
  // adding event listeners for forms
  document.getElementById('createUserBtn').addEventListener('click', createNewUser);
  document.getElementById('updateUserBtn').addEventListener('click', updateUserInfo);
  // document.getElementById('deleteUserBtn').addEventListener('click', deleteTargetUser);
  // $('#userTable').DataTable();
  console.log("Loaded functions!");
} //todo: sort show/hide for aesthetics
// function deleteUser(){
//   $("#userView").hide(500);
//   $("#tktView").hide(500);
//   $("#createUserForm").hide(500);
//   $("#err-message").hide();
//   $("#updateUserForm").hide(500);
//   $("#deleteUserForm").show(500);
// }
function createUser(){
  $("#userView").hide(500);
  $("#tktView").hide(500);
  $("#createUserForm").show(500);
  $("#err-message").hide();
  $("#updateUserForm").hide(500);
  // $("#deleteUserForm").hide(500);
}
function updateUser(){
  $("#updateUserForm").show(500);
  $("#userView").hide(5000);
  $("#tktView").hide(500);
  $("#createUserForm").hide(500);
  $("#err-message").hide();
  // $("#deleteUserForm").hide(500);
}
function viewUsers(){
  console.log("Viewing users...");
  $("#userView").show(500);
  $("#tktView").hide(500);
  $("#createUserForm").hide(500);
  $("#err-message").hide();
  $("#updateUserForm").hide(500);

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
}

function hideAll(){
  console.log("Hiding everything...");
  $("#err-message").hide(0);
  $("#userView").hide(0);
  $("#tktView").hide(0);
  $("#createUserForm").hide(0);
  $("#updateUserForm").hide(0);
  // $("#deleteUserForm").hide(500);
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

function deleteTargetUser(email){
  console.log('Deleting user...');
  // let aa = document.getElementById('emailDe').value;
  let aa = email;
  let userTarget = {
    email: aa
  }
  console.log(userTarget);
  let userJSON = JSON.stringify(userTarget); //jsonify
  let xhr = new XMLHttpRequest();
  xhr.open('DELETE', 'users');
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


function updateUserInfo(id){
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
  let gg = document.getElementById('IDUp').value;

  //making the object to jsonification
  let editUser = {
      username: aa,
      password: bb,
      firstName: cc,
      lastName: dd,
      email: ee,
      role: ff,
      id: gg
  }
  console.log(editUser);
  let userJSON = JSON.stringify(editUser); //jsonify
  let xhr = new XMLHttpRequest();
  xhr.open('PUT', 'users');
  // third parameter (default true) indicates we want to make this req async
  xhr.setRequestHeader('Content-type', 'application/json');
  xhr.send(userJSON);
  xhr.onreadystatechange = function() {
    console.log("State: " + xhr.readyState);
    if (xhr.readyState == 4 && xhr.status == 204) {
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

        document.getElementById('err-message').innerText = "Creation successful! Refreshing...";
        errorView();
        setTimeout(function(){
          location.reload();
        }, 3000); //3 second timer

        //update the user table: https://datatables.net/reference/api/row.add()
        // newUser.id="NEW"; //bug patch
        // var table = $('#userTable').DataTable();
        // var rowNode = table
        //     .row.add(newUser)
        //     .draw()
        //     .node();
        // $( rowNode )
        //     .css( 'color', 'red' )
        //     .animate( { color: 'black' } );
        //
        // viewUsers();

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
        let table = $('#userTable').DataTable( {
          "autoWidth": false,
          "paging": false,
            data: userJSON,
            columns: [
                { data: 'id' },
                { data: 'username' },
                { data: 'password' },
                { data: 'firstName' },
                { data: 'lastName' },
                { data: 'email' },
                { data: 'roleName' },
                { data: null,
                defaultContent:
                  "<a class='tableDelBtn' href='#'><span data-feather='trash-2'></span></a><a class='tableEditBtn' href='#'><span data-feather='edit-2'></span></a>"
                } //we're adding buttons to the form here
            ]
        } );
        //there was a bug using bootstrap with the datatables...
        //data poured into the table fine, but it reset the width(css)
        //turns out, datatables rewrote the classname that bootstrap needed
        //so we'll patch this by forcing the table to have both classes:
          //also had to disable autowidth in the table init above
            //or maybe should've done that to begin with
        $("#userTable").addClass("table table-striped table-sm");

        //re-initialize feather replacing
        feather.replace();
        //add button logic to the table
        $('#userTable tbody').on( 'click', '.tableDelBtn', function () { //from datatable api
          var data = table.row( $(this).parents('tr') ).data(); //creates an object
          //confirm for deletion
          let conf = confirm("Are you sure you wish to delete "+ data.username+" ?");
          if(conf){
            //if so, send to normal delete method
            deleteTargetUser(data.email);
          }

        } );

        $('#userTable tbody').on( 'click', '.tableEditBtn', function () { //from datatable api
          var data = table.row( $(this).parents('tr') ).data(); //creates an object
          //replaces the ID value in the update form with the selected row's id value
          document.getElementById('IDUp').value = data.id;
          //also replaces values in the other bits to what was in the data
          document.getElementById('emailUp').value = data.email;
          document.getElementById('usernameUp').value = data.username;
          document.getElementById('passwordUp').value = data.password;
          document.getElementById('firstNameUp').value = data.firstName;
          document.getElementById('lastNameUp').value = data.lastName;
          // document.getElementById('roleUp').value = data.roleName;
          //then swap views to the update form
          updateUser();
        } );


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
          "paging": false,
            data: ticketJSON,
            columns: [
                { data: 'id' },
                { data: 'author' },
                { data: 'amount'},
                { data: 'submittedStr' },
                { data: 'resolvedStr' },
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
function logout() {
    console.log('logout invoked!');
    let xhr = new XMLHttpRequest();
    xhr.open('GET', 'auth');
    xhr.send();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 204) {
            console.log('logout successful!');
            window.location.assign("index.html");
        }
    }
}
