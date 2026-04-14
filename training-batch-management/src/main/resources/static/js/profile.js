async function loadProfile() {

    const token = localStorage.getItem("token");

    const res = await fetch("http://localhost:8080/profile/profile", {
        headers: {
            "Authorization": "Bearer " + token
        }
    });

    const user = await res.json();

    document.getElementById("name").value = user.name;
    document.getElementById("email").value = user.email;
	document.getElementById("phone").value = user.phone;
}

async function updateProfile() {

    const token = localStorage.getItem("token");

    const res = await fetch("http://localhost:8080/profile/profile", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify({
            name: document.getElementById("name").value,
            email: document.getElementById("email").value,
			phone : document.getElementById("phone").value
        })
    });

    if (res.ok) {
        alert("Profile updated ✅");
    } else {
        alert("Update failed ❌");
    }
}

async function changePassword() {

    const token = localStorage.getItem("token");

    const res = await fetch("http://localhost:8080/profile/change-password", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify({
            oldPassword: document.getElementById("oldPassword").value,
            newPassword: document.getElementById("newPassword").value
        })
    });

    const msg = await res.text();

    if (res.ok) {
        alert(msg);
    } else {
        alert(msg);
    }
}

window.onload = loadProfile;