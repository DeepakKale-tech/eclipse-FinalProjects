async function loadBatches() {

    const token = localStorage.getItem("token");

    const res = await fetch("http://localhost:8080/student/batches", {
        headers: { "Authorization": "Bearer " + token }
    });

    const batches = await res.json();

    let table = document.getElementById("batchTable");
    table.innerHTML = "";

    batches.forEach(batch => {

        let topics = batch.topics || [];

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
            </td>
        </tr>`;

        table.innerHTML += row;
    });
}

async function loadNotifications() {

    const token = localStorage.getItem("token");

    const res = await fetch("http://localhost:8080/student/notifications", {
        headers: { "Authorization": "Bearer " + token }
    });

    const data = await res.json();

    let list = document.getElementById("notifications");
    let count = document.getElementById("count");

    list.innerHTML = "";
    count.innerText = data.length;

    data.forEach(n => {
        list.innerHTML += `<li class="list-group-item">${n.message}</li>`;
    });
}

window.onload = function () {
    loadBatches();
    loadNotifications();
};