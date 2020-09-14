
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
function resolveFormView(){
  console.log("Showing form...")
  $("#resolveTicketForm").show(500);
  $("#tktView").hide(500);
  $("#err-message").hide();
}
function viewTkts(){
  console.log("Viewing tickets...");
  $("#resolveTicketForm").hide(500);
  $("#tktView").show(500);
  $("#err-message").hide();
}

function hideAll(){
  console.log("Hiding everything...");
  $("#resolveTicketForm").hide(500);
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

function resolveTicket(){
  console.log('Resolving Ticket...');
  let id = document.getElementById('ticketID').value;
  let status = document.getElementById('ticketStatus').value;
  //note about status value
  //changing the value here to ints, for the statusID instead of status
    //makes it convenient for sql stuff later
  let solution = {
    id: id,
    statusID: status
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
        console.log("201")
        console.log("Sent resolution.")

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
