async function loadBatches() {

    const token = localStorage.getItem("token");

    const res = await fetch("http://localhost:8080/student/batches", {
        headers: { "Authorization": "Bearer " + token }
    });

    if (!res.ok) {
        console.error("Failed to load batches ❌");
        return;
    }

    const batches = await res.json();

    let table = document.getElementById("batchTable");
    table.innerHTML = "";

    for (let batch of batches) {

        // 🔥 FETCH TOPICS SEPARATELY
        const topicRes = await fetch(`http://localhost:8080/student/topics/${batch.id}`, {
            headers: { "Authorization": "Bearer " + token }
        });

        let topics = [];
        if (topicRes.ok) {
            topics = await topicRes.json();
        }

        let topicList = topics.map(t =>
            `<li>${t.topicName} ${t.completed ? "✅" : "❌"}</li>`
        ).join("");

        if (topics.length === 0) {
            topicList = "<li>No topics yet</li>";
        }

        let row = `
        <tr>
            <td>${batch.batchName}</td>

            <td>
                <div class="progress">
                    <div class="progress-bar bg-success" style="width:${batch.progressPercentage}%">
                        ${batch.progressPercentage}%
                    </div>
                </div>
            </td>

            <td>
                <ul>${topicList}</ul>
            </td>
        </tr>`;

        table.innerHTML += row;
    }
}

async function loadNotifications() {

    const token = localStorage.getItem("token");

    const res = await fetch("http://localhost:8080/student/notifications", {
        headers: { "Authorization": "Bearer " + token }
    });

    if (!res.ok) {
        console.error("Notification error ❌");
        return;
    }

    const data = await res.json();

    let list = document.getElementById("notifications");
    let count = document.getElementById("count");

    list.innerHTML = "";

    const unread = data.filter(n => !n.readStatus);
    count.innerText = unread.length;

    data.forEach(n => {
        list.innerHTML += `
        <li class="list-group-item d-flex justify-content-between align-items-center"
            onclick="markAsRead(${n.id})" style="cursor:pointer">
            
            ${n.message}
            
            ${!n.readStatus 
                ? '<span class="badge bg-danger">NEW</span>' 
                : '<span class="badge bg-success">✔</span>'}
        </li>`;
    });
}

async function markAsRead(id) {

    const token = localStorage.getItem("token");

    const res = await fetch(`http://localhost:8080/student/notifications/read/${id}`, {
        method: "PUT",
        headers: { "Authorization": "Bearer " + token }
    });

    if (res.ok) {
        loadNotifications(); // refresh UI
    } else {
        alert("Failed to update notification ❌");
    }
}

function logout() {
    localStorage.removeItem("token");
    window.location.href = "login.html"; // or your login route
}

window.onload = function () {
    loadBatches();
    loadNotifications();
};