const API = "";

const fallbackTrucks = [
  { truckId: 1, source: "Chennai", destination: "Bangalore", departureDate: "2026-05-12", capacity: 18, availableCapacity: 18, truckType: "Container", rating: 4.8, pricePerTon: 4200, status: "AVAILABLE", estimatedArrivalTime: "2026-05-13T10:00:00" },
  { truckId: 2, source: "Hyderabad", destination: "Pune", departureDate: "2026-05-14", capacity: 12, availableCapacity: 8, truckType: "Open Body", rating: 4.6, pricePerTon: 3600, status: "PARTIALLY_BOOKED", estimatedArrivalTime: "2026-05-15T08:30:00" },
  { truckId: 3, source: "Mumbai", destination: "Ahmedabad", departureDate: "2026-05-15", capacity: 20, availableCapacity: 20, truckType: "Refrigerated", rating: 4.9, pricePerTon: 5100, status: "AVAILABLE", estimatedArrivalTime: "2026-05-16T11:45:00" }
];

// --- CORE UTILITIES ---
function qs(selector) { return document.querySelector(selector); }
function qsa(selector) { return [...document.querySelectorAll(selector)]; }

function toast(message) {
  const el = document.createElement("div");
  el.className = "toast";
  el.textContent = message;
  document.body.appendChild(el);
  setTimeout(() => el.remove(), 2600);
}

function currentUser() {
  return JSON.parse(localStorage.getItem("loadifyUser") || "null");
}

function logout() {
  localStorage.removeItem("loadifyUser");
  location.href = "/index.html";
}

// --- API & DATA HANDLING ---
async function api(path, options = {}) {
  const res = await fetch(API + path, {
    headers: { "Content-Type": "application/json" },
    ...options
  });
  const body = await res.json();
  if (!res.ok) throw new Error(body.message || "Request failed");
  return body.data;
}

// --- ADMIN & ROLE LOGIC (NEW) ---
function initAdminNavigation() {
    const user = currentUser();
    // 1. Check if user exists and has admin role
    if (user && user.role === 'admin') {
        const nav = document.querySelector('nav');
        if (nav) {
            const adminLink = document.createElement('a');
            adminLink.href = "/pages/admin-dashboard.html";
            adminLink.innerHTML = "🛡️ Admin Panel";
            adminLink.style.color = "#fb923c";
            adminLink.style.fontWeight = "900";
            nav.prepend(adminLink); // Puts Admin Panel at the top of the list
        }
    }
}

// Function to calculate Admin Stats (Day/Month/Year)
async function getAdminStats(filterType = 'day') {
    // Note: In a real app, this would be an API call. 
    // Here we simulate by pulling from localStorage data.
    const allBookings = JSON.parse(localStorage.getItem("loadify_bookings") || "[]");
    const allPosts = JSON.parse(localStorage.getItem("loadify_trucks") || "[]");
    
    const now = new Date();
    
    const filtered = allBookings.filter(item => {
        const itemDate = new Date(item.createdAt || Date.now());
        if (filterType === 'day') return itemDate.toDateString() === now.toDateString();
        if (filterType === 'month') return itemDate.getMonth() === now.getMonth() && itemDate.getFullYear() === now.getFullYear();
        if (filterType === 'year') return itemDate.getFullYear() === now.getFullYear();
        return true;
    });

    const totalAmount = filtered.reduce((sum, item) => sum + (Number(item.totalPrice) || 0), 0);
    return {
        total: totalAmount,
        count: filtered.length,
        postsCount: allPosts.length
    };
}

// Function to handle feedback submissions
async function submitFeedback(data) {
    const feedbacks = JSON.parse(localStorage.getItem("loadify_feedbacks") || "[]");
    feedbacks.push({
        ...data,
        timestamp: new Date().toISOString(),
        userId: currentUser()?.userId || 'Guest'
    });
    localStorage.setItem("loadify_feedbacks", JSON.stringify(feedbacks));
    toast("Feedback submitted successfully!");
}

// --- UI COMPONENTS ---
function truckCard(truck, allowBook = true) {
  return `
    <article class="card truck-card">
      <div class="route"><span>${truck.source}</span><span>-></span><span>${truck.destination}</span></div>
      <div class="meta">
        <span>Available: ${truck.departureDate}</span>
        <span>Capacity: ${truck.availableCapacity ?? truck.capacity} / ${truck.capacity} tons</span>
        <span>Type: ${truck.truckType || "Heavy Truck"}</span>
        <span class="stars">★★★★★ <b>${truck.rating || 4.5}</b></span>
        <span>Price: Rs. ${truck.pricePerTon || 0}/ton</span>
      </div>
      <button class="btn btn-primary" data-book="${truck.truckId}" ${allowBook ? "" : "disabled"}>Book</button>
      <button class="btn btn-dark" data-quick="${truck.truckId}">Quick View</button>
    </article>`;
}

async function loadTrucks(target = "#truckResults", params = new URLSearchParams()) {
  let trucks;
  try {
    const data = await api(`/trucks/search?${params.toString()}`);
    trucks = data.content || [];
  } catch {
    trucks = fallbackTrucks.filter(t => {
      const source = params.get("source")?.toLowerCase();
      const destination = params.get("destination")?.toLowerCase();
      const date = params.get("date");
      const capacity = Number(params.get("capacity") || 0);
      return (!source || t.source.toLowerCase().includes(source)) &&
        (!destination || t.destination.toLowerCase().includes(destination)) &&
        (!date || t.departureDate === date) &&
        (!capacity || t.availableCapacity >= capacity);
    });
  }
  const box = qs(target);
  if (box) box.innerHTML = trucks.length ? trucks.map(t => truckCard(t)).join("") : `<div class="card">No available return trucks found.</div>`;
}

function bindSearch(formSelector, target) {
  const form = qs(formSelector);
  if (!form) return;
  form.addEventListener("submit", e => {
    e.preventDefault();
    const params = new URLSearchParams(new FormData(form));
    loadTrucks(target, params);
  });
}

// --- INITIALIZATION ---
document.addEventListener("DOMContentLoaded", () => {
    initAdminNavigation();
});

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
  if (quickId) toast("Detailed route, driver and insurance information is available on the booking page.");
});

window.Loadify = { 
    api, 
    toast, 
    currentUser, 
    logout, 
    loadTrucks, 
    bindSearch,
    getAdminStats,
    submitFeedback,
    initAdminNavigation
};

// --- ADMIN & FEEDBACK LOGIC ---

// This function filters data by Day, Month, or Year for the Admin Tally
async function getAdminStats(filterType = 'day') {
    const allBookings = JSON.parse(localStorage.getItem("loadify_bookings") || "[]");
    const allTrucks = JSON.parse(localStorage.getItem("loadify_trucks") || "[]");
    const now = new Date();

    const filtered = allBookings.filter(b => {
        const d = new Date(b.createdAt || Date.now());
        if (filterType === 'day') return d.toDateString() === now.toDateString();
        if (filterType === 'month') return d.getMonth() === now.getMonth() && d.getFullYear() === now.getFullYear();
        return d.getFullYear() === now.getFullYear();
    });

    return {
        total: filtered.reduce((sum, b) => sum + (Number(b.totalPrice) || 0), 0),
        offered: allTrucks.reduce((sum, t) => sum + (Number(t.pricePerTon) * Number(t.capacity) || 0), 0),
        count: filtered.length
    };
}

// This function saves user feedback to LocalStorage
async function submitFeedback(data) {
    const feedbacks = JSON.parse(localStorage.getItem("loadify_feedbacks") || "[]");
    feedbacks.push({
        ...data,
        timestamp: new Date().toISOString(),
        userId: currentUser()?.userId || 'Guest'
    });
    localStorage.setItem("loadify_feedbacks", JSON.stringify(feedbacks));
}

// This function injects the Admin link into the sidebar if the role is ADMIN
function initAdminNavigation() {
    const user = currentUser();
    // Use "ADMIN" in caps to match your Java UserRole Enum
    if (user && user.role === 'ADMIN') {
        const nav = document.querySelector('nav');
        if (nav && !document.getElementById('adminLink')) {
            const adminLink = document.createElement('a');
            adminLink.id = 'adminLink';
            adminLink.href = "/pages/admin-dashboard.html";
            adminLink.innerHTML = "🛡️ Admin Panel";
            adminLink.style.color = "#fb923c"; // Orange theme
            adminLink.style.fontWeight = "900";
            nav.prepend(adminLink); 
        }
    }
}

// Auto-run admin check whenever a page with app.js loads
document.addEventListener("DOMContentLoaded", initAdminNavigation);
