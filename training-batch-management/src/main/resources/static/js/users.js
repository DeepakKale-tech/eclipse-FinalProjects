const params = new URLSearchParams(window.location.search);
const userId = params.get("id");

window.onload = loadUser;

async function loadUser() {

    const token = localStorage.getItem("token");

    const res = await fetch(`http://localhost:8080/admin/user/${userId}`, {
        headers: {
            "Authorization": "Bearer " + token
        }
    });

    const user = await res.json();

    document.getElementById("name").value = user.name;
    document.getElementById("email").value = user.email;
	document.getElementById("phone").value = user.phone;
}

async function updateUser() {

    const token = localStorage.getItem("token");
	
	const name = document.getElementById("name").value.trim();
	const email = document.getElementById("email").value.trim();
	const phone = document.getElementById("phone").value.trim();
	const password = document.getElementById("password").value.trim();
	
	if (!name || !email || !phone) {
	        alert("Name, Email and Phone required ❌");
	        return;
	    }
		
		const user = {
		       name: name,
		       email: email,
			   phone : phone
		   };
		   
	
			  const res = await fetch(`http://localhost:8080/admin/user/${userId}`, {
			          method: "PUT",
			          headers: {
			              "Content-Type": "application/json",
			              "Authorization": "Bearer " + token
			          },
			          body: JSON.stringify(user)
			      });

			      if (res.ok) {
			          alert("User updated ✅");
			          window.location.href = "admin.html";
			      } else {
			          alert("Update failed ❌");
			      }
}