const URL = 'http://localhost:9000';


function getUsers() {
  return fetch(URL + '/users').then(data => { return data.json(); })
}

function fecthUsers() {
  getUsers().then(function(data) {
    document.getElementById('users-list').innerHTML = data.map(r => r.firstname + "<br>");
  })
}

