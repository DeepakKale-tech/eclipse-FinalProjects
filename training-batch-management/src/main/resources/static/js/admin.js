async function loadUsers() {

    const token = localStorage.getItem("token");

    const response = await fetch("http://localhost:8080/admin/users", {
        headers: {
            "Authorization": "Bearer " + token
        }
    });

    const users = await response.json();

    let table = document.getElementById("userTable");
    table.innerHTML = "";

    users.forEach(user => {
        let row = `<tr>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.role}</td>
        </tr>`;
        table.innerHTML += row;
    });
}

async function createUser() {

    const token = localStorage.getItem("token");

    const user = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
        role: document.getElementById("role").value
    };

    const response = await fetch("http://localhost:8080/admin/create-user", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token   // ✅ IMPORTANT
        },
        body: JSON.stringify(user)
    });

    if (response.ok) {
        alert("User created ✅");
        loadUsers(); // refresh table
    } else {
        alert("Error creating user ❌");
    }
}

// load on page open
window.onload = loadUsers;