console.log("admin.js loaded ✅");

function goProfile() {
    window.location.href = "profile.html";
}
async function createBatch() {

    const token = localStorage.getItem("token");

    const trainerId = document.getElementById("trainerId").value; // input field

	let url = `http://localhost:8080/admin/batch/${trainerId}`;
	let method = "POST";

	if (window.editingBatchId) {
	    url = `http://localhost:8080/admin/batch/update/${window.editingBatchId}/${trainerId}`;
	    method = "PUT";
	}
	const startTime = document.getElementById("startTime").value;
	const endTime = document.getElementById("endTime").value;
	
    const batch = {
        batchName: document.getElementById("batchName").value,
        domain: {id :document.getElementById("domain").value},
		timing: startTime + " to " + endTime,
        startDate: document.getElementById("startDate").value,
        endDate: document.getElementById("endDate").value,
		status: document.getElementById("status").value
    };

    const response = await fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify(batch)
    });

	window.editingBatchId = null;
    if (response.ok) {
        alert("Batch created successfully ✅");
        loadBatches(); // refresh list
    } else {
        alert("Failed to create batch ❌");
    }
}

async function loadTrainers() {
	
	const token = localStorage.getItem("token");
	
	const res = await fetch("http://localhost:8080/admin/users", {
	       headers: {
	           "Authorization": "Bearer " + token
	       }
	   });
    const users = await res.json();

    let dropdown = document.getElementById("trainerId");
    dropdown.innerHTML = "";

    users.filter(u => u.role === "TRAINER")
        .forEach(trainer => {
            dropdown.innerHTML += `<option value="${trainer.id}">${trainer.name}</option>`;
        });
}

async function loadBatches() {

	const token = localStorage.getItem("token");
    const res = await fetch("http://localhost:8080/admin/batches",{
		headers: {
			"Authorization" : "Bearer " + token
		} 
	});
	
	if (!res.ok) {
	        console.error("❌ Failed to load batches", res.status);
	        return;
	    }

    const data = await res.json();

    let table = document.getElementById("batchTable");
    table.innerHTML = "";

    data.forEach(b => {
        table.innerHTML += `
        <tr>
            <td>${b.id}</td>
            <td>${b.batchName}</td>
            <td>${b.domain.name}</td>
            <td>${b.timing}</td>
            <td>${b.startDate}</td>
            <td>${b.endDate}</td>
            <td>${b.status}</td>
			<td>${b.trainer ? b.trainer.name : "N/A"}</td>
			<td>
			    <button class="btn btn-sm btn-warning" onclick="editBatch(${b.id})">Edit</button>
				<button class="btn btn-sm btn-danger" onclick="deleteBatch(${b.id})">Delete</button>
			</td>
        </tr>`;
    });
}

async function deleteBatch(id) {

    const token = localStorage.getItem("token");

    if (!confirm("Delete this batch?")) return;

    const res = await fetch(`http://localhost:8080/admin/batch/${id}`, {
        method: "DELETE",
        headers: {
            "Authorization": "Bearer " + token
        }
    });

    if (res.ok) {
        alert("Deleted ✅");
        loadBatches();
    } else {
        alert("Delete failed ❌");
    }
}

function editBatch(id) {
	window.location.href = `batch-edit.html?id=${id}`;
}



async function searchTrainer() {
	const token = localStorage.getItem("token");
    const name = document.getElementById("searchName").value;

    const res = await fetch(`/admin/trainer/search?name=${name}`,{
		    headers: {
		        "Authorization": "Bearer " + token
		    }
		});
    const data = await res.json();

    console.log(data);
}

async function searchBatch() {

	const token = localStorage.getItem("token");
    const domain = document.getElementById("searchDomain").value;

    const res = await fetch(`http://localhost:8080/admin/batch/search?domain=${domain}`,{
	    headers: {
	        "Authorization": "Bearer " + token
	    }
	});
    const data = await res.json();

    let table = document.getElementById("batchTable");
    table.innerHTML = "";

    data.forEach(b => {
        table.innerHTML += `
        <tr>
            <td>${b.id}</td>
            <td>${b.batchName}</td>
            <td>${b.domain.name}</td>
            <td>${b.timing}</td>
            <td>${b.startDate}</td>
            <td>${b.endDate}</td>
            <td>${b.status}</td>
            <td>
                <button class="btn btn-warning btn-sm" onclick="editBatch(${b.id})">Edit</button>
                <button class="btn btn-danger btn-sm" onclick="deleteBatch(${b.id})">Delete</button>
            </td>
        </tr>`;
    });
}

async function loadHistory() {

	const token = localStorage.getItem("token");
    const res = await fetch("http://localhost:8080/admin/batches/completed", {
		headers: {
			"Authorization": "Bearer " + token
		}
	});
    const data = await res.json();

    let table = document.getElementById("historyTable");
    table.innerHTML = "";

    data.forEach(b => {
        table.innerHTML += `
        <tr>
            <td>${b.batchName}</td>
            <td>${b.domain.name}</td>
            <td>${b.status}</td>
        </tr>`;
    });
}

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
			<td>${user.phone}</td>
            <td>${user.role}</td>
			<td>
			     <button class="btn btn-warning btn-sm" onclick="window.location.href='users.html?id=${user.id}'">Edit</button>
			     <button class="btn btn-danger btn-sm" onclick="deleteUser(${user.id})">Delete</button>
			</td>
        </tr>`;
        table.innerHTML += row;
    });
}

async function createUser() {

    const token = localStorage.getItem("token");

	const name = document.getElementById("name").value.trim();
	const email = document.getElementById("email").value.trim();
	const phone = document.getElementById("phone").value.trim();
	const password = document.getElementById("password").value.trim();

	if (!name || !email || !password || !phone) {
	    alert("All fields are required ❌");
	    return;
	}
	let url = "http://localhost:8080/admin/create-user";
	let method = "POST";

	if (window.editingUserId) {
	    url = `http://localhost:8080/admin/user/${window.editingUserId}`;
	    method = "PUT";
	}
	
    const user = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
		phone: document.getElementById("phone").value,
        password: document.getElementById("password").value,
        role: document.getElementById("role").value
    };

    const response = await fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token   // ✅ IMPORTANT
        },
        body: JSON.stringify(user)
    });

	const msg = await response.text();
	
    if (response.ok) {
        alert(window.editingUserId ?"User updated ✅" :"User created ✅");
		// RESET FORM 🔥
		document.getElementById("name").value = "";
		document.getElementById("email").value = "";
		document.getElementById("phone").value = "";
		document.getElementById("password").value = "";
		document.getElementById("role").value = "STUDENT";

		window.editingUserId = null;
        loadUsers(); // refresh table
    } else {
        alert(msg);
    }
	window.editingUserId = null;
}

async function editUser(id) {

	const res = await fetch("http://localhost:8080/admin/users", {
	        headers: {
	            "Authorization": "Bearer " + token
	        }
	    });
    const users = await res.json();

	
    const u = users.find(x => x.id === id);

    document.getElementById("name").value = u.name;
    document.getElementById("email").value = u.email;
	document.getElementById("phone").value = u.phone;
    document.getElementById("role").value = u.role;

	document.getElementById("password").value = "";
	
    window.editingUserId = id;
}

async function deleteUser(id) {

    const token = localStorage.getItem("token");

    if (!confirm("Delete user?")) return;

    const res = await fetch(`http://localhost:8080/admin/user/${id}`, {
        method: "DELETE",
        headers: {
            "Authorization": "Bearer " + token
        }
    });

    if (res.ok) {
        alert("User deleted ✅");
        loadUsers();
    } else {
        alert("Delete failed ❌");
    }
}

async function addDomain() {
	
	const token = localStorage.getItem("token");
	
	const name = document.getElementById("newDomain").value.trim();
	
	if(!name)
		{
			alert("Domain Name required ❌");
			return;
		}
		
	try{
		const res = await fetch("http://localhost:8080/admin/domain", {
				        method: "POST",
				        headers: {
				            "Content-Type": "application/json",
				            "Authorization": "Bearer " + token
				        },
				        body: JSON.stringify({ name })
				    });
					
				const msg = await res.text();
					
				if (res.ok)
					 {
				     	alert("Domain added ✅");
						document.getElementById("newDomain").value = "";
				     	loadDomains();
				 	 }else 
					 {
					    alert(msg); 
					  }
	}
	catch(err)
	{
		console.error(err);
		alert("Server error ❌")
	}
}


async function loadDomains() {

    const token = localStorage.getItem("token");

    const res = await fetch("http://localhost:8080/admin/domains", {
        headers: {
            "Authorization": "Bearer " + token
        }
    });

    const domains = await res.json();

    let dropdown = document.getElementById("domain");
    dropdown.innerHTML = "";

    domains.forEach(d => {
        dropdown.innerHTML += `<option value="${d.id}">${d.name}</option>`;
    });
}


function logout() {
    localStorage.removeItem("token");
    window.location.href = "login.html"; // or your login route
}

// load on page open

window.onload = function () {
    loadUsers();
    loadBatches();
    loadTrainers();
    loadHistory();
	loadDomains();
};