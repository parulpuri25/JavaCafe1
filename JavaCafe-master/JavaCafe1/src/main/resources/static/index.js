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
      });
  
    // Fetch and insert footer
    fetch('footer.html')
      .then(response => response.text())
      .then(data => {
        document.getElementById('footer').innerHTML = data;
      });
  });


    