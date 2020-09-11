



function roleCheck(){
    console.log("Function called");
    console.log(sessionStorage.getItem("role"));
}

//
//function getPokemon() {
//    var pokemon;
//    var pokemonID = document.getElementById("searchstuff").value;
//    var pokemonURL = "https://pokeapi.co/api/v2/pokemon/" + pokemonID;
//
//    // Send a GET request to this endpoint
//
//
//    fetch(pokemonURL)
//        // This Promise returns the Object in the body
//        .then(response => { return response.json(); })
//        // Then we do something with that Object, called data here
//        .then(data => {
//            console.log(data);
//            pokemon = data;
//
//            var paragraph = document.querySelector("p");
//            //paragraph.textContent = pokemon.abilities[0].ability.name;
//
//            var imageElement = document.createElement("img");
//            imageElement.src = pokemon.sprites.front_default;
//            var divElement = document.querySelector("#spriteDiv");
//            divElement.appendChild(imageElement);
//
//            /////////////////////////////
//            //document.write(data);
//            return;
//        } )
//        .catch(exception => console.log(exception));
//
//        // Fetch -> gets us a Promise with a HTTP response
//        // .then
//        // response.json() -> gets us a Promise with the Object in its body
//        // .then
//        // do something with that Object
//
//
//}

