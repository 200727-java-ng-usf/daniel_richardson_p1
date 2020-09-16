
window.onload = function() {
  console.log("Loading functions...");
  // hide the main table on dashboard load
  hideAll();
  // building the tables with data
  getTicketData();
  viewTkts();

  // adding event listeners for side bar actions
  document.getElementById('submitTicketNav').addEventListener('click', submitTicketView);
  document.getElementById('editTicketNav').addEventListener('click', editTicketView);
  document.getElementById('viewTicketsNav').addEventListener('click', viewTkts);
  // adding event listeners for forms
  document.getElementById('editTicketBtn').addEventListener('click', editTicket);
  document.getElementById('submitTicketBtn').addEventListener('click', submitTicket);
  document.getElementById('logoutNav').addEventListener('click', logout);
  // $('#userTable').DataTable();
  console.log("Loaded functions!");
} //todo: sort show/hide for aesthetics
function submitTicketView(){
  console.log("Showing form...")
  $("#editTicketForm").hide(500);
  $("#submitTicketForm").show(500);
  $("#tktView").hide(500);
  $("#err-message").hide();
}
function editTicketView(){
  console.log("Showing form...")
  $("#editTicketForm").show(500);
  $("#submitTicketForm").hide(500);
  $("#tktView").hide(500);
  $("#err-message").hide();
}
function viewTkts(){
  console.log("Viewing tickets...");
  $("#editTicketForm").hide(500);
  $("#submitTicketForm").hide(500);
  $("#tktView").show(500);
  $("#err-message").hide();
}

function hideAll(){
  console.log("Hiding everything...");
  $("#editTicketForm").hide(500);
  $("#submitTicketForm").hide(500);
  $("#tktView").hide(500);
  $("#err-message").hide();
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

function editTicket(){
  console.log('Editing Ticket...');
  let aa = document.getElementById("ticketIDEd").value;
  console.log(aa);
  let bb = document.getElementById("amountEd").value;
  console.log(bb);
  let cc = document.getElementById("descEd").value;
  console.log(cc);
  let dd = document.getElementById("typeEd").value;
  console.log(dd);

  let solution = {
    id: aa,
    amount: bb,
    description: cc,
    typeID: dd
  }
  console.log(solution);
  let solutionJSON = JSON.stringify(solution); //jsonify
  let xhr = new XMLHttpRequest();
  xhr.open('PUT', 'tickets');
  // third parameter (default true) indicates we want to make this req async
  xhr.setRequestHeader('Content-type', 'application/json');
  xhr.send(solutionJSON);
  //
  xhr.onreadystatechange = function() {
    console.log("State: " + xhr.readyState);
    if (xhr.readyState == 4 && xhr.status == 201) {
        console.log("201");
        console.log("Sent resolution.");

        //todo: fiddle with DataTable api to update row
        //for now, just refresh the page to invoke the pageload functions
        document.getElementById('err-message').innerText = "Update successful! Refreshing...";
        errorView();
        setTimeout(function(){
          location.reload();
        }, 3000); //3 second timer


    } else if(xhr.readyState == 4 && xhr.status != 201){
      errorView();
      let err = JSON.parse(xhr.responseText);
      document.getElementById('err-message').innerText = err.message;
      console.log("Error");
    }
  }

}

function submitTicket(){
  console.log('Submitting New Ticket...');
  let aa = document.getElementById('amountSu').value;
  let bb = document.getElementById('descSu').value;
  let cc = document.getElementById('typeSu').value;

  let solution = {
    amount: aa,
    description: bb,
    typeID: cc
  }
  console.log(solution);
  let solutionJSON = JSON.stringify(solution); //jsonify
  let xhr = new XMLHttpRequest();
  xhr.open('POST', 'tickets');
  // third parameter (default true) indicates we want to make this req async
  xhr.setRequestHeader('Content-type', 'application/json');
  xhr.send(solutionJSON);
  //
  xhr.onreadystatechange = function() {
    console.log("State: " + xhr.readyState);
    if (xhr.readyState == 4 && xhr.status == 201) {
        console.log("201");
        console.log("Sent ticket.");

        //todo: fiddle with DataTable api to update row
        //for now, just refresh the page to invoke the pageload functions
        document.getElementById('err-message').innerText = "Submission successful! Refreshing...";
        errorView();
        setTimeout(function(){
          location.reload();
        }, 3000); //3 second timer


    } else if(xhr.readyState == 4 && xhr.status != 201){
      errorView();
      let err = JSON.parse(xhr.responseText);
      document.getElementById('err-message').innerText = err.message;
      console.log("Error");
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
                { data: 'amount' },
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
