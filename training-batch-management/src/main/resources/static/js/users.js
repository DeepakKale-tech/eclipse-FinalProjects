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
    console.log("Update button clicked ✅");

    const name = document.getElementById("name").value.trim();
    const email = document.getElementById("email").value.trim();
    let phone = document.getElementById("phone").value.trim();
    const password = document.getElementById("password").value.trim();

    console.log({ name, email, password, phone });

    if (!name || !email || !phone) {
        if (typeof showToast === "function") showToast("Name, Email and Phone required", "warning");
        else alert("Name, Email and Phone required ❌");
        return;
    }

    phone = phone.replace("+91", "");

    const user = {
        name: name,
        email: email,
        phone: phone,
        password: password
    };

    try {
		const res = await fetch(`http://localhost:8080/admin/user/${userId}`, {
		        method: "PUT",
		        headers: {
		            "Content-Type": "application/json",
		            "Authorization": "Bearer " + token
		        },
		        body: JSON.stringify(user)
		    });

			console.log("Status:", res.status); 
			
			const text = await res.text();        // 🔥 always read response
			console.log("Response text:", text);
		    if (res.ok) {
		        if (typeof showToast === "function") showToast("User updated", "success");
		        else alert("User updated ✅");
				
				setTimeout(() => {
				        window.location.href = "admin.html";
				    }, 1000);
		        
		    } else {
		        if (typeof showToast === "function") showToast("Update failed", "error");
		        else alert("Update failed ❌");
		    }
			
    } catch (err) {
		console.error("JS Error ❌", err);
		       alert("Something broke ❌");
    }
    
}