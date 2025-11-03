const perks = [
    {pid: 1, title: "10% off Movies", discount: "10%", product: "Movies", membership: "Visa", expiry: "2025-12-31", upvotes: 0, downvotes: 0},
    {pid: 2, title: "Free Flight", discount: "100%", product: "Domestic Flight", membership: "Air Miles", expiry: "2026-01-15", upvotes: 0, downvotes: 0},
    {pid: 3, title: "15% off Coffee", discount: "15%", product: "Coffee", membership: "Mastercard", expiry: "2025-11-15", upvotes: 0, downvotes: 0}
  ];
  
  const listDiv = document.getElementById('perk-list');
  const searchInput = document.getElementById('search');
  
  function renderList(filteredPerks) {
    listDiv.innerHTML = '';
    filteredPerks.forEach(perk => {
      const div = document.createElement('div');
      div.className = 'perk-card';
      div.innerHTML = `
        <h3>${perk.title}</h3>
        <p><strong>Discount:</strong> ${perk.discount}</p>
        <p><strong>Product:</strong> ${perk.product}</p>
        <p><strong>Membership:</strong> ${perk.membership}</p>
        <p><strong>Expiry:</strong> ${perk.expiry}</p>
        <p>
          <button onclick="upvote(${perk.pid})">👍 ${perk.upvotes}</button>
          <button onclick="downvote(${perk.pid})">👎 ${perk.downvotes}</button>
        </p>
      `;
      listDiv.appendChild(div);
    });
  }
  
  function upvote(pid) {
    const perk = perks.find(p => p.pid === pid);
    perk.upvotes++;
    renderList(filteredPerks());
  }
  
  function downvote(pid) {
    const perk = perks.find(p => p.pid === pid);
    perk.downvotes++;
    renderList(filteredPerks());
  }
  
  function filteredPerks() {
    const term = searchInput.value.toLowerCase();
    return perks.filter(p => p.product.toLowerCase().includes(term));
  }
  
  renderList(perks);
  
  searchInput.addEventListener('input', () => {
    renderList(filteredPerks());
  });
  