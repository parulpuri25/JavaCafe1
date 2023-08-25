// Function to read the login status cookie and update the UI
function updateLoginStatusUI(isLoggedIn) {
    var userIcon = document.querySelector('#user-icon');
    var accountCard = document.querySelector('#account-card'); // Add this line

    if (isLoggedIn) {
		getUserName(accountCard)
		userIcon.onclick = () =>{
		if(accountCard.style.display == "block"){
			accountCard.style.display = "none";
		}
		else{
        accountCard.style.display = "block";
    }}} else {
        userIcon.onclick = () => {
            window.location.href = "login.html";
        };
    }
}
 function getUserName(accountCard)   {
    fetch("/users/userDetails")
    .then(response => response.json())
    .then(data =>{
		const isLoggedInUser = data.isLoggedInUser;
		console.log("", isLoggedInUser)
		updateWelcomeMessage(accountCard,isLoggedInUser)
	});
	} 
function updateWelcomeMessage(accountCard, isLoggedInUser){
	const welcomepara = accountCard.querySelector('p');
	welcomepara.textContent = 'Welcome '+ isLoggedInUser;
	
}
function checkLoginStatus() {
    fetch("/users/loginStatus")
        .then(response => response.json())
        .then(data => {
            const isLoggedIn = data.isLoggedIn;
            updateLoginStatusUI(isLoggedIn);
            console.log("",isLoggedIn);
        })
        .catch(error => {
            console.error('Error fetching login status: ', error);
        });
}

document.addEventListener('DOMContentLoaded', function() {
    // Fetch and insert header
    fetch('header.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById('header').innerHTML = data;
            // Add the event listener for search icon after inserting the header
            let search = document.querySelector('.search-bar');
            document.querySelector('#search-icon').onclick = () => {
                console.log('Clicked!');
                search.classList.toggle('active');
            };

            // Call checkLoginStatus initially and then at regular intervals
            checkLoginStatus();
            setInterval(checkLoginStatus, 5000); // Poll every 5 seconds
        });

    // Fetch and insert footer
    fetch('footer.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById('footer').innerHTML = data;
        });
   
});
