async function loadUsers() {

    const token = localStorage.getItem("token");

    const response = await fetch("http://localhost:8080/admin/users", {
        headers: {
            "Authorization": "Bearer " + token
        }
    });

    const users = await response.json();

    let table = document.querySelector("#userTable tbody");
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

// load on page open
window.onload = loadUsers;