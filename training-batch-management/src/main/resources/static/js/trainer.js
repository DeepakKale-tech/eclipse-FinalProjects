async function createBatch() {

    const token = localStorage.getItem("token");

    const batch = {
        batchName: document.getElementById("batchName").value,
        startDate: document.getElementById("startDate").value,
        endDate: document.getElementById("endDate").value,
        status: "ONGOING"
    };

    const response = await fetch("http://localhost:8080/trainer/create-batch/2", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify(batch)
    });

    if (response.ok) {
        alert("Batch created ✅");
        loadBatches();
        loadNotifications();
    } else {
        alert("Error creating batch ❌");
    }
}

async function completeTopic(topicId) {

    const token = localStorage.getItem("token");

    const response = await fetch(`http://localhost:8080/trainer/complete-topic/${topicId}`, {
        method: "PUT",
        headers: {
            "Authorization": "Bearer " + token
        }
    });

    if (response.ok) {
        alert("Topic completed ✅");
        loadBatches();
        loadNotifications();
    }
}

async function loadBatches() {

    const token = localStorage.getItem("token");

    const response = await fetch("http://localhost:8080/trainer/batches", {
        headers: {
            "Authorization": "Bearer " + token
        }
    });

    const batches = await response.json();

    let table = document.querySelector("#batchTable tbody");
    table.innerHTML = "";

    if (batches.length === 0) {
        table.innerHTML = "<tr><td colspan='2'>No batches found</td></tr>";
        return;
    }

    batches.forEach(batch => {

        let row = `
        <tr>
            <td>${batch.batchName}</td>
            <td>
                <div style="background:#ddd; width:100%; border-radius:5px;">
                    <div style="background:green; width:${batch.progressPercentage}%; color:white; text-align:center; border-radius:5px;">
                        ${batch.progressPercentage}%
                    </div>
                </div>
            </td>
        </tr>`;

        table.innerHTML += row;
    });
}

async function loadNotifications() {

    const token = localStorage.getItem("token");

    const response = await fetch("http://localhost:8080/trainer/notifications", {
        headers: {
            "Authorization": "Bearer " + token
        }
    });

    const data = await response.json();

    let list = document.getElementById("notifications");
    let count = document.getElementById("count");

    list.innerHTML = "";
    count.innerText = data.length;

    if (data.length === 0) {
        list.innerHTML = "<li>No notifications</li>";
        return;
    }

    data.forEach(n => {
        list.innerHTML += `<li>${n.message}</li>`;
    });
}

window.onload = function () {
    loadBatches();
    loadNotifications();
};