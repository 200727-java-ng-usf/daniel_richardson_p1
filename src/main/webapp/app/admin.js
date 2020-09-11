
window.onload = function() {
  console.log("Loaded functions");
  // adding event listeners when page loads
  document.getElementById('viewTickets').addEventListener('click', show);
  document.getElementById('home').addEventListener('click', hide);
  // document.getElementById('login-button').addEventListener('click', alert1);
  document.getElementById('mainView').setAttribute('hidden', true);

  // document.getElementById('login-message').setAttribute('hidden', true);
}

function alert1(){
  alert("Test");
}

function show(){
  document.getElementById('mainView').removeAttribute('hidden', true);
}

function hide(){
  document.getElementById('mainView').setAttribute('hidden', true);
}
