const params = new URLSearchParams(window.location.search);
const batchId = params.get("id");

window.onload = async function () {
    loadDomains();
    loadTrainers();
    loadBatch();
};

function getHeaders() {
	
	const token = localStorage.getItem("token");
	console.log("TOKEN:", localStorage.getItem("token"));
    return {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + token
    };
}

// 🔹 Load batch details
async function loadBatch() {

    const res = await fetch(`http://localhost:8080/admin/batch/${batchId}`, {
        headers: getHeaders()
    });

    if (!res.ok) {
		console.error("Error:", res.status);
        if (typeof showToast === "function") showToast("Failed to load batch", "error");
        else alert("Failed to load batch ❌");
        return;
    }

    const b = await res.json();

	console.log("Batch trainer:", b.trainer);
	
    let startTime = "", endTime = "";

    if (b.timing && b.timing.includes("to")) {
        const times = b.timing.split(" to ");
        startTime = times[0];
        endTime = times[1];
    }

    document.getElementById("batchName").value = b.batchName || "";
    document.getElementById("domain").value = b.domain ? b.domain.id : "";
    document.getElementById("status").value = b.status || "";
    document.getElementById("startTime").value = startTime;
    document.getElementById("endTime").value = endTime;
    document.getElementById("startDate").value = b.startDate || "";
    document.getElementById("endDate").value = b.endDate || "";
    document.getElementById("trainerId").value = b.trainer ? b.trainer.id : "";
}

// 🔹 Load domains
async function loadDomains() {

    const res = await fetch("http://localhost:8080/admin/domains", {
        headers: getHeaders()
    });

    const data = await res.json();

    const dropdown = document.getElementById("domain");
    dropdown.innerHTML = "";

    data.forEach(d => {
        dropdown.innerHTML += `<option value="${d.id}">${d.name}</option>`;
    });
}

// 🔹 Load trainers
async function loadTrainers() {

    const res = await fetch("http://localhost:8080/admin/trainers", {
        headers: getHeaders()
    });

    const data = await res.json();

	console.log("Trainers API Data:", data);
    const dropdown = document.getElementById("trainerId");
    dropdown.innerHTML = "";

    data.forEach(t => {
        dropdown.innerHTML += `<option value="${t.id}">${t.name}</option>`;
    });
	console.log("Dropdown HTML:", dropdown.innerHTML);
}

// 🔹 Update batch
async function updateBatch() {

    const batchName = document.getElementById("batchName").value;
    const domainId = document.getElementById("domain").value;
    const trainerId = document.getElementById("trainerId").value;

    if (!batchName || !domainId || !trainerId) {
        if (typeof showToast === "function") showToast("Fill required fields", "warning");
        else alert("Fill required fields ❌");
        return;
    }

    const batch = {
        batchName: batchName,
        domain: { id: domainId },
        timing: document.getElementById("startTime").value + " to " +
                document.getElementById("endTime").value,
        startDate: document.getElementById("startDate").value,
        endDate: document.getElementById("endDate").value,
        status: document.getElementById("status").value
    };

    const res = await fetch(
        `http://localhost:8080/admin/batch/update/${batchId}/${trainerId}`,
        {
            method: "PUT",
            headers: getHeaders(),
            body: JSON.stringify(batch)
        }
    );

    if (res.ok) {
        if (typeof showToast === "function") showToast("Batch updated", "success");
        else alert("Batch updated ✅");
        window.location.href = "admin.html";
    } else {
        if (typeof showToast === "function") showToast("Update failed", "error");
        else alert("Update failed ❌");
    }
}