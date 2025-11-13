// Wait for DOM to be fully loaded
document.addEventListener('DOMContentLoaded', () => {
    const perks = [
        { pid: 1, title: "10% off Movies", discount: "10%", product: "Movies", membership: "Visa", expiry: "2025-12-31", upvotes: 0, downvotes: 0 },
        { pid: 2, title: "Free Flight", discount: "100%", product: "Domestic Flight", membership: "Air Miles", expiry: "2026-01-15", upvotes: 0, downvotes: 0 },
        { pid: 3, title: "15% off Coffee", discount: "15%", product: "Coffee", membership: "Mastercard", expiry: "2025-11-15", upvotes: 0, downvotes: 0 }
    ];

    const listDiv = document.getElementById('perk-list');
    const searchInput = document.getElementById('search');

    // Check if elements exist
    if (!listDiv || !searchInput) {
        console.error('Required elements not found: perk-list or search');
        return;
    }

    // Render the perks
    function renderList(filteredPerks) {
        listDiv.innerHTML = '';

        filteredPerks.forEach(perk => {
            const div = document.createElement('div');
            div.className = 'perk-card';
            div.dataset.pid = perk.pid;

            div.innerHTML = `
                <h3>${perk.title}</h3>
                <p><strong>Discount:</strong> ${perk.discount}</p>
                <p><strong>Product:</strong> ${perk.product}</p>
                <p><strong>Membership:</strong> ${perk.membership}</p>
                <p><strong>Expiry:</strong> ${perk.expiry}</p>
                <p>
                    <button class="upvote-btn">👍 ${perk.upvotes}</button>
                    <button class="downvote-btn">👎 ${perk.downvotes}</button>
                </p>
            `;

            listDiv.appendChild(div);
        });
    }

    // Event delegation for upvote/downvote
    listDiv.addEventListener('click', (e) => {
        const card = e.target.closest('.perk-card');
        if (!card) return;

        const pid = parseInt(card.dataset.pid);
        const perk = perks.find(p => p.pid === pid);
        if (!perk) return;

        if (e.target.classList.contains('upvote-btn')) {
            perk.upvotes++;
        } else if (e.target.classList.contains('downvote-btn')) {
            perk.downvotes++;
        }

        renderList(filteredPerks());
    });

    // Filter perks based on search
    function filteredPerks() {
        const term = searchInput.value.toLowerCase();
        return perks.filter(p => p.product.toLowerCase().includes(term));
    }

    // Initial render
    renderList(perks);

    // Update list as user types
    searchInput.addEventListener('input', () => {
        renderList(filteredPerks());
    });

    // Add perk functionality
    const addPerkBtn = document.getElementById('addPerkBtn');
    const perkNameInput = document.getElementById('perkName');
    const discountInput = document.getElementById('discount');
    const membershipInput = document.getElementById('membership');
    const productTypeInput = document.getElementById('productType');
    const expiryDateInput = document.getElementById('expiryDate');
    const perkMessage = document.getElementById('perkMessage');

    if (addPerkBtn && perkNameInput && productTypeInput && expiryDateInput && discountInput && membershipInput) {
        addPerkBtn.addEventListener('click', () => {
            const title = perkNameInput.value.trim();
            const product = productTypeInput.value.trim();
            const discount = discountInput.value.trim();
            const membership = membershipInput.value;
            const expiry = expiryDateInput.value;

            // Validation
            if (!title || !product || !discount || !expiry || !membership) {
                perkMessage.textContent = 'Please fill in all fields (Perk Name, Discount, Product Type, Membership, and Expiry Date)';
                perkMessage.style.color = '#ff6b6b';
                return;
            }

            // Create new perk
            const newPid = perks.length > 0 ? Math.max(...perks.map(p => p.pid)) + 1 : 1;
            const newPerk = {
                pid: newPid,
                title: title,
                discount: discount,
                product: product,
                membership: membership,
                expiry: expiry,
                upvotes: 0,
                downvotes: 0
            };

            // Add to perks array
            perks.push(newPerk);

            // Clear form
            perkNameInput.value = '';
            discountInput.value = '';
            membershipInput.selectedIndex = 0; // Reset to placeholder
            productTypeInput.value = '';
            expiryDateInput.value = '';

            // Render updated list
            renderList(filteredPerks());

            // Show success message
            perkMessage.textContent = 'Perk added successfully!';
            perkMessage.style.color = '#00BFFF';
            setTimeout(() => {
                perkMessage.textContent = '';
            }, 3000);
        });
    }

    // Remove perk functionality
    const removePerkBtn = document.getElementById('removePerkBtn');
    if (removePerkBtn) {
        removePerkBtn.addEventListener('click', () => {
            const title = perkNameInput.value.trim();

            if (!title) {
                perkMessage.textContent = 'Please enter a perk name to remove';
                perkMessage.style.color = '#ff6b6b';
                return;
            }

            const index = perks.findIndex(p => p.title.toLowerCase() === title.toLowerCase());
            if (index === -1) {
                perkMessage.textContent = 'Perk not found';
                perkMessage.style.color = '#ff6b6b';
                return;
            }

            perks.splice(index, 1);
            perkNameInput.value = '';
            renderList(filteredPerks());

            perkMessage.textContent = 'Perk removed successfully!';
            perkMessage.style.color = '#00BFFF';
            setTimeout(() => {
                perkMessage.textContent = '';
            }, 3000);
        });
    }
});
