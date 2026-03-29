let stompClient = null;
let chartInstance = null;
let calendarInstance = null;

// ================= CREATE BATCH =================
async function createBatch() {
    const token = localStorage.getItem("token");

    const batch = {
        batchName: document.getElementById("batchName").value,
        startDate: document.getElementById("startDate").value,
        endDate: document.getElementById("endDate").value,
        status: "ONGOING"
    };

    const res = await fetch("http://localhost:8080/trainer/create-batch", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify(batch)
    });

    if (res.ok) {
        alert("Batch created ✅");
        loadAll();
    } else {
        alert("Error creating batch ❌");
    }
}

// ================= LOAD ALL =================
function loadAll() {
    loadBatches();
    loadNotifications();
    loadChart();
    loadCalendar();
}

// ================= LOAD BATCHES =================
async function loadBatches() {
    const token = localStorage.getItem("token");

    const res = await fetch("http://localhost:8080/trainer/batches", {
        headers: { "Authorization": "Bearer " + token }
    });

    const batches = await res.json();

    let table = document.getElementById("batchTable");
    table.innerHTML = "";

    for (let batch of batches) {

        const topicRes = await fetch(`http://localhost:8080/trainer/topics/${batch.id}`, {
            headers: { "Authorization": "Bearer " + token }
        });

        const topics = await topicRes.json();

        let topicOptions = topics.map(t =>
            `<option value="${t.id}">${t.topicName}</option>`
        ).join("");

        let topicList = topics.map(t =>
            `<li>${t.topicName} ${t.completed ? "✅" : "❌"}</li>`
        ).join("");

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

                <input class="form-control mb-1" id="topicName-${batch.id}" placeholder="New Topic">

                <button class="btn btn-sm btn-primary mb-1" onclick="addTopic(${batch.id})">
                    Add Topic
                </button>

                <select class="form-select mb-1" id="topic-${batch.id}">
                    ${topicOptions}
                </select>

                <button class="btn btn-sm btn-success" onclick="completeSelectedTopic(${batch.id})">
                    Complete
                </button>
            </td>
        </tr>`;

        table.innerHTML += row;
    }
}

// ================= ADD TOPIC =================
async function addTopic(batchId) {
    const token = localStorage.getItem("token");
    const topicName = document.getElementById(`topicName-${batchId}`).value;

    const res = await fetch(`http://localhost:8080/trainer/add-topic/${batchId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify({
            topicName: topicName,
            description: "From UI"
        })
    });

    if (res.ok) {
        alert("Topic added ✅");
        loadBatches();
    }
}

// ================= COMPLETE TOPIC =================
async function completeSelectedTopic(batchId) {
    const token = localStorage.getItem("token");
    const topicId = document.getElementById(`topic-${batchId}`).value;

    const res = await fetch(`http://localhost:8080/trainer/complete-topic/${topicId}`, {
        method: "PUT",
        headers: { "Authorization": "Bearer " + token }
    });

    if (res.ok) {
        alert("Completed ✅");
        loadAll();
    }
}

// ================= NOTIFICATIONS =================
async function loadNotifications() {
    const token = localStorage.getItem("token");

    const res = await fetch("http://localhost:8080/trainer/notifications", {
        headers: { "Authorization": "Bearer " + token }
    });

    const data = await res.json();

    let list = document.getElementById("notifications");
    let count = document.getElementById("count");

    list.innerHTML = "";

    const unread = data.filter(n => !n.readStatus);
    count.innerText = unread.length;

    data.forEach(n => {
        list.innerHTML += `
            <li class="list-group-item" onclick="markAsRead(${n.id})">
                ${n.message} ${!n.readStatus ? "🔴" : "✅"}
            </li>`;
    });
}

async function markAsRead(id) {
    const token = localStorage.getItem("token");

    await fetch(`http://localhost:8080/trainer/notifications/read/${id}`, {
        method: "PUT",
        headers: { "Authorization": "Bearer " + token }
    });

    loadNotifications();
}

// ================= WEBSOCKET =================
function connectSocket(userId) {
    let socket = new SockJS("http://localhost:8080/ws");
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {
        stompClient.subscribe(`/topic/notifications/${userId}`, function (msg) {
            let notification = JSON.parse(msg.body);
            showLiveNotification(notification);
        });
    });
}

async function initSocket() {
    const token = localStorage.getItem("token");

    const res = await fetch("http://localhost:8080/trainer/me", {
        headers: { "Authorization": "Bearer " + token }
    });

    const user = await res.json();
    connectSocket(user.id);
}

function showLiveNotification(n) {
    let list = document.getElementById("notifications");
    let count = document.getElementById("count");

    list.innerHTML = `<li class="list-group-item">${n.message} 🔴</li>` + list.innerHTML;
    count.innerText = parseInt(count.innerText) + 1;
}

// ================= CHART =================
async function loadChart() {
    const token = localStorage.getItem("token");

    const res = await fetch("http://localhost:8080/trainer/batches", {
        headers: { "Authorization": "Bearer " + token }
    });

    const batches = await res.json();

    const labels = batches.map(b => b.batchName);
    const data = batches.map(b => b.progressPercentage);

    if (chartInstance) chartInstance.destroy();

    chartInstance = new Chart(document.getElementById("progressChart"), {
        type: "bar",
        data: {
            labels,
            datasets: [{
                label: "Progress %",
                data
            }]
        }
    });
}

// ================= CALENDAR =================
async function loadCalendar() {
    const token = localStorage.getItem("token");

    const res = await fetch("http://localhost:8080/trainer/batches", {
        headers: { "Authorization": "Bearer " + token }
    });

    const batches = await res.json();

    let events = batches.map(b => ({
        title: b.batchName,
        start: b.startDate,
        end: b.endDate
    }));

    if (calendarInstance) calendarInstance.destroy();

    calendarInstance = new FullCalendar.Calendar(
        document.getElementById("calendar"),
        {
            initialView: "dayGridMonth",
            events
        }
    );

    calendarInstance.render();
}

// ================= INIT =================
window.onload = function () {
    loadAll();
    initSocket();
};