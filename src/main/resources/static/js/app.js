const API = "";

// ---------------- UTILITIES ----------------

function qs(selector) {
  return document.querySelector(selector);
}

function qsa(selector) {
  return [...document.querySelectorAll(selector)];
}

function toast(message) {
  const el = document.createElement("div");
  el.className = "toast";
  el.textContent = message;

  document.body.appendChild(el);

  setTimeout(() => {
    el.remove();
  }, 2600);
}

function currentUser() {
  return JSON.parse(localStorage.getItem("loadifyUser") || "null");
}

function logout() {
  localStorage.removeItem("loadifyUser");
  location.href = "/index.html";
}

// ---------------- API ----------------

async function api(path, options = {}) {

  const res = await fetch(API + path, {
    headers: {
      "Content-Type": "application/json"
    },
    ...options
  });

  const body = await res.json();

  if (!res.ok) {
    throw new Error(body.message || "Request failed");
  }

  return body.data;
}

// ---------------- ADMIN NAVIGATION ----------------

function initAdminNavigation() {

  const user = currentUser();

  if (user && user.role === 'ADMIN') {

    const nav = document.querySelector('nav');

    if (nav && !document.getElementById('adminLink')) {

      const adminLink = document.createElement('a');

      adminLink.id = 'adminLink';

      adminLink.href = "/pages/admin-dashboard.html";

      adminLink.innerHTML = "🛡️ Admin Panel";

      adminLink.style.color = "#fb923c";

      adminLink.style.fontWeight = "900";

      nav.prepend(adminLink);
    }
  }
}

// ---------------- ADMIN STATS ----------------

async function getAdminStats(filterType = 'day') {

  const allBookings = JSON.parse(localStorage.getItem("loadify_bookings") || "[]");

  const allTrucks = JSON.parse(localStorage.getItem("loadify_trucks") || "[]");

  const now = new Date();

  const filteredBookings = allBookings.filter(b => {

    const d = new Date(b.createdAt || Date.now());

    if (filterType === 'day') {
      return d.toDateString() === now.toDateString();
    }

    if (filterType === 'month') {
      return d.getMonth() === now.getMonth() &&
             d.getFullYear() === now.getFullYear();
    }

    return d.getFullYear() === now.getFullYear();
  });

  return {
    total: filteredBookings.reduce((sum, b) =>
      sum + (Number(b.totalPrice) || 0), 0),

    offered: allTrucks.reduce((sum, t) =>
      sum + ((Number(t.pricePerTon) || 0) * (Number(t.capacity) || 0)), 0),

    count: filteredBookings.length
  };
}

// ---------------- FEEDBACK ----------------

async function submitFeedback(data) {

  console.log("Submitting feedback:", data);

  const response = await fetch('/api/user/feedback', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
  });

  console.log("Response:", response);

  if (!response.ok) {
    throw new Error("Failed to submit feedback");
  }

  toast("Feedback submitted successfully!");

  return await response.text();
}

// ---------------- UI COMPONENTS ----------------

function truckCard(truck, allowBook = true) {

  return `
    <article class="card truck-card">

      <div class="route">
        <span>${truck.source}</span>
        <span>-></span>
        <span>${truck.destination}</span>
      </div>

      <div class="meta">
        <span>Available: ${truck.departureDate}</span>

        <span>
          Capacity:
          ${truck.availableCapacity ?? truck.capacity}
          /
          ${truck.capacity} tons
        </span>

        <span>Type: ${truck.truckType || "Heavy Truck"}</span>

        <span class="stars">
          ★★★★★ <b>${truck.rating || 4.5}</b>
        </span>

        <span>
          Price: Rs. ${truck.pricePerTon || 0}/ton
        </span>
      </div>

      <button class="btn btn-primary"
              data-book="${truck.truckId}"
              ${allowBook ? "" : "disabled"}>
        Book
      </button>

      <button class="btn btn-dark"
              data-quick="${truck.truckId}">
        Quick View
      </button>

    </article>
  `;
}

// ---------------- LOAD TRUCKS ----------------

async function loadTrucks(
  target = "#truckResults",
  params = new URLSearchParams()
) {

  const box = qs(target);

  if (!box) return;

  // Loading UI
  box.innerHTML = `
    <div class="card">
      Loading available trucks...
    </div>
  `;

  try {

    const data = await api(`/trucks/search?${params.toString()}`);

    const trucks = data.content || [];

    // No trucks found
    if (trucks.length === 0) {

      box.innerHTML = `
        <div class="card">
          No trucks available for the selected route/date.
        </div>
      `;

      return;
    }

    // Render trucks
    box.innerHTML = trucks
      .map(t => truckCard(t))
      .join("");

  } catch (error) {

    console.error("Truck loading failed:", error);

    box.innerHTML = `
      <div class="card error-card">
        <h3>Unable to Load Trucks</h3>
        <p>
          We could not fetch truck data right now.
          Please try again later.
        </p>

        <button class="btn btn-primary"
                onclick="location.reload()">
          Retry
        </button>
      </div>
    `;

    toast("Failed to load trucks");
  }
}

// ---------------- SEARCH ----------------

function bindSearch(formSelector, target) {

  const form = qs(formSelector);

  if (!form) return;

  form.addEventListener("submit", e => {

    e.preventDefault();

    const params = new URLSearchParams(new FormData(form));

    loadTrucks(target, params);
  });
}

// ---------------- INITIALIZATION ----------------

document.addEventListener("DOMContentLoaded", () => {

  initAdminNavigation();
});

// ---------------- GLOBAL CLICK EVENTS ----------------

document.addEventListener("click", e => {

  const bookId = e.target.dataset.book;

  const quickId = e.target.dataset.quick;

  if (bookId) {

    if (!currentUser()) {

      localStorage.setItem("pendingTruckId", bookId);

      location.href = "/pages/login.html";

      return;
    }

    localStorage.setItem("pendingTruckId", bookId);

    location.href = "/pages/booking.html";
  }

  if (quickId) {

    toast(
      "Detailed route, driver and insurance information is available on the booking page."
    );
  }
});

function formatINR(value) {
    if (value == null) return "₹ 0";

    return new Intl.NumberFormat("en-IN", {
        style: "currency",
        currency: "INR",
        maximumFractionDigits: 0
    }).format(value);
}

// ---------------- GLOBAL EXPORT ----------------

window.Loadify = {
  api,
  toast,
  currentUser,
  logout,
  loadTrucks,
  bindSearch,
  getAdminStats,
  submitFeedback,
  initAdminNavigation,
  formatINR
};