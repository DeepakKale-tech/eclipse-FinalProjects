let stompClient = null;
let chartInstance = null;
let calendarInstance = null;

// ================= CREATE BATCH =================
async function createBatch() {
    const token = localStorage.getItem("token");

    const batchName = document.getElementById("batchName").value;

    if (!batchName || batchName.trim() === "") {
        alert("Batch name required ❌");
        return;
    }

    const batch = {
        batchName: batchName,
		domain: {id :document.getElementById("domain").value},
		timing: document.getElementById("startTime").value + " to " + document.getElementById("endTime").value,
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

async function loadDomains() {

    const token = localStorage.getItem("token");

    const res = await fetch("http://localhost:8080/trainer/domains", {
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
// ================= LOAD ALL =================
function loadAll() {
    loadBatches();
    loadNotifications();
    loadChart();
    loadCalendar();
	loadDomains();
}


// ================= LOAD STUDENTS =================
async function loadStudents() {
    const token = localStorage.getItem("token");

    const res = await fetch("http://localhost:8080/trainer/students", {
        headers: { "Authorization": "Bearer " + token }
    });

    if (!res.ok) return [];

    return await res.json();
}

// ================= LOAD BATCHES =================
async function loadBatches() {
    const token = localStorage.getItem("token");

    try {
        const res = await fetch("http://localhost:8080/trainer/batches", {
            headers: { "Authorization": "Bearer " + token }
        });

        const batches = await res.json();
        const students = await loadStudents();

        // ✅ UPDATE COUNTS
        document.getElementById("totalBatches").innerText = batches.length;
        document.getElementById("totalStudents").innerText = students.length;

        let table = document.getElementById("batchTable");
        table.innerHTML = "";

        for (let batch of batches) {

            // ===== TOPICS =====
            let topics = [];
            try {
                const topicRes = await fetch(`http://localhost:8080/trainer/topics/${batch.id}`, {
                    headers: { "Authorization": "Bearer " + token }
                });
                topics = await topicRes.json();
            } catch {
                topics = [];
            }

            let topicOptions = topics.length > 0
                ? topics.map(t => `<option value="${t.id}"  ${t.completed ? "disabled" : ""}> 
					${t.topicName} ${t.completed ? "(✔)" : ""}</option>`).join("")
                : `<option disabled>No topics</option>`;

            let topicList = topics.length > 0
                ? topics.map(t => `<li>${t.topicName} ${t.completed ? "✅" : "❌"}</li>`).join("")
                : "<li>No topics</li>";

            // ===== STUDENTS =====
            let studentList = batch.students?.length > 0
                ? batch.students.map(s => `<li>${s.name}</li>`).join("")
                : "<li>No students</li>";

            let studentOptions = students.length > 0
                ? students.map(s => `<option value="${s.id}">${s.name}</option>`).join("")
                : `<option disabled>No students</option>`;

            // ===== ROW =====
            let row = `
            <tr>
                <td>${batch.batchName}</td>

                <td>
                    <div class="progress">
                        <div class="progress-bar bg-success" style="width:${batch.progressPercentage || 0}%">
                            ${batch.progressPercentage || 0}%
                        </div>
                    </div>
                </td>

                <td>
                    <ul>${topicList}</ul>

                    <input class="form-control mb-1" id="topicName-${batch.id}" placeholder="New Topic">

                    <button class="btn btn-sm btn-primary mb-1" onclick="addTopic(${batch.id})">
                        Add
                    </button>

                    <select class="form-select mb-1" id="topic-${batch.id}">
                        ${topicOptions}
                    </select>

                    <button class="btn btn-sm btn-success" onclick="completeSelectedTopic(${batch.id})">
                        Complete
                    </button>
					
					${batch.status === "COMPLETED" 
					    ? `<button class="btn btn-sm btn-secondary mt-1" disabled>Completed ✔</button>`
					    : `<button class="btn btn-sm btn-dark mt-1" onclick="completeBatch(${batch.id})">
					        ✔ Mark Batch Completed
					       </button>`
					}
					
					<div id="batchMsg-${batch.id}" class="small mt-1 text-muted"></div>
                </td>

                <td>
                    <ul>${studentList}</ul>

                    <select class="form-select mb-1" id="student-${batch.id}">
                        ${studentOptions}
                    </select>

                    <button class="btn btn-sm btn-warning" onclick="assignStudent(${batch.id})">
                        Assign
                    </button>
                </td>
            </tr>`;

            table.innerHTML += row;
        }

    } catch (e) {
        console.error("Batch load error ❌", e);
    }
}

function loadChart() {
    const token = localStorage.getItem("token");

    fetch("http://localhost:8080/trainer/batches", {
        headers: { "Authorization": "Bearer " + token }
    })
    .then(res => res.json())
    .then(batches => {

        if (!batches || batches.length === 0) return;

        const labels = batches.map(b => b.batchName);
        const data = batches.map(b => b.progressPercentage || 0);

        const canvas = document.getElementById("progressChart");
        const ctx = canvas.getContext("2d");

        // 🔥 REMOVE BLUR EFFECT (solid clean color)
        if (chartInstance) chartInstance.destroy();

        chartInstance = new Chart(ctx, {
            type: "bar",
            data: {
                labels: labels,
                datasets: [{
                    label: "Progress %",
                    data: data,
                    backgroundColor: "#4f46e5",
                    borderRadius: 12,   // smooth edges
                    barThickness: 40    // thicker bars (important!)
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false, // 🔥 prevents stretch

                plugins: {
                    legend: {
                        display: false
                    }
                },

                layout: {
                    padding: 20 // spacing
                },

                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            color: "#374151",
                            font: {
                                size: 12
                            }
                        },
                        grid: {
                            color: "rgba(0,0,0,0.08)"
                        }
                    },
                    x: {
                        ticks: {
                            color: "#374151",
                            font: {
                                size: 12
                            }
                        },
                        grid: {
                            display: false
                        }
                    }
                }
            }
        });

    })
    .catch(err => console.error("Chart error ❌", err));
}

function loadCalendar() {
    const token = localStorage.getItem("token");

    fetch("http://localhost:8080/trainer/batches", {
        headers: { "Authorization": "Bearer " + token }
    })
    .then(res => res.json())
    .then(batches => {

        const calendarEl = document.getElementById("calendar");

        if (!calendarEl) {
            console.error("Calendar div not found ❌");
            return;
        }

        let events = batches.map(b => ({
            title: b.batchName,
            start: b.startDate,
            end: b.endDate
        }));

        if (calendarInstance) calendarInstance.destroy();

        calendarInstance = new FullCalendar.Calendar(calendarEl, {
            initialView: "dayGridMonth",
            height: 500,
            events: events
        });

        calendarInstance.render();
    })
    .catch(err => console.error("Calendar error ❌", err));
}

// ================= ADD TOPIC =================
async function addTopic(batchId) {
    const token = localStorage.getItem("token");
    const topicName = document.getElementById(`topicName-${batchId}`).value;

    if (!topicName || topicName.trim() === "") {
        alert("Topic cannot be empty ❌");
        return;
    }

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

    if (!topicId) return;

    const res = await fetch(`http://localhost:8080/trainer/complete-topic/${topicId}`, {
        method: "PUT",
        headers: { "Authorization": "Bearer " + token }
    });

    if (res.ok) {
        loadAll();
    }
}

async function completeBatch(batchid) {

    const token = localStorage.getItem("token");

	const msgBox = document.getElementById('batchMsg-${batchId}'); 
    // assume batchId stored globally or from URL
    //const batchId = localStorage.getItem("batchId");

    // Step 1: Check topics
    const topicRes = await fetch(`http://localhost:8080/trainer/topics/${batchId}`, {
        headers: {
            "Authorization": "Bearer " + token
        }
    });

    const topics = await topicRes.json();	
	
    if (!topics || topics.length === 0) {
        msgBox.innerText = "No topics found ❌";
        return;
    }

    const allCompleted = topics.every(t => t.completed === true);

    if (!allCompleted) {
        msgBox.innerText ="Complete all topics before marking batch as completed ⚠️";
        return;
    }

    // Step 2: Update batch status
    const res = await fetch(`http://localhost:8080/trainer/batch/complete/${batchId}`, {
        method: "PUT",
        headers: {
            "Authorization": "Bearer " + token
        }
    });

    if (res.ok) {
        msgBox.innerText ="Batch marked as COMPLETED ✅";
    } else {
        msgBox.innerText ="Error updating batch ❌";
    }
}
// ================= ASSIGN STUDENT =================
async function assignStudent(batchId) {
    const token = localStorage.getItem("token");
    const studentId = document.getElementById(`student-${batchId}`).value;

    const res = await fetch(
        `http://localhost:8080/trainer/assign-student/${batchId}/${studentId}`,
        {
            method: "POST",
            headers: { "Authorization": "Bearer " + token }
        }
    );

    if (res.ok) {
        alert("Assigned ✅");
        loadBatches();
    }
}

// ================= NOTIFICATIONS =================
async function loadNotifications() {
    const token = localStorage.getItem("token");

    const res = await fetch("http://localhost:8080/trainer/notifications", {
        headers: { "Authorization": "Bearer " + token }
    });

    if (!res.ok) return;

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

            <div>
                <strong>${n.message}</strong><br>
                <small>${new Date(n.timestamp).toLocaleString()}</small>
            </div>

            ${!n.readStatus 
                ? '<span class="badge bg-danger">NEW</span>' 
                : '<span class="badge bg-success">✔</span>'}
        </li>`;
    });
}

// ================= MARK AS READ =================
async function markAsRead(id) {
    const token = localStorage.getItem("token");

    const res = await fetch(`http://localhost:8080/trainer/notifications/read/${id}`, {
        method: "PUT",
        headers: { "Authorization": "Bearer " + token }
    });

    if (res.ok) {
        loadNotifications();
    }
}

// ================= WEBSOCKET =================
function connectWebSocket() {
    const socket = new SockJS("http://localhost:8080/ws");
    stompClient = Stomp.over(socket);

    stompClient.connect({
        Authorization: "Bearer " + localStorage.getItem("token")
    }, function () {

        fetch("http://localhost:8080/trainer/me", {
            headers: { "Authorization": "Bearer " + localStorage.getItem("token") }
        })
        .then(res => res.json())
        .then(user => {

            stompClient.subscribe(`/topic/notifications/${user.id}`, function (msg) {
                const notification = JSON.parse(msg.body);
                alert("🔔 " + notification.message);
                loadNotifications();
            });
        });

    }, function (error) {
        console.error("WebSocket Error ❌", error);
    });
}

// ================= LOGOUT =================
function logout() {
    localStorage.removeItem("token");
    window.location.href = "login.html";
}

// ================= INIT =================
window.onload = function () {
    loadAll();
    connectWebSocket();
};