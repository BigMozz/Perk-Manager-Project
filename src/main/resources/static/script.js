// Wait for DOM to be fully loaded
document.addEventListener('DOMContentLoaded', () => {
    // Get perks from server-side data, or use fallback perks if empty/undefined
    const defaultPerks = [
        { pid: 1, title: "10% off Movies", discount: "10%", product: "Movies", membership: "Visa", expiry: "2025-12-31", upvotes: 0, downvotes: 0 },
        { pid: 2, title: "Free Flight", discount: "100%", product: "Domestic Flight", membership: "Air Miles", expiry: "2026-01-15", upvotes: 0, downvotes: 0 },
        { pid: 3, title: "15% off Coffee", discount: "15%", product: "Coffee", membership: "Mastercard", expiry: "2025-11-15", upvotes: 0, downvotes: 0 }
    ];

    // Use serverPerks if it exists and has items, otherwise use fallback
    // Check if serverPerks is undefined, null, or empty array
    const serverPerks = window.serverPerks;
    let perksToUse;
    if (serverPerks === undefined || serverPerks === null || !Array.isArray(serverPerks) || serverPerks.length === 0) {
        perksToUse = defaultPerks;
    } else {
        perksToUse = serverPerks;
    }

    // Get login status - check multiple sources
    const isLoggedIn = window.isLoggedIn === true ||
        window.isLoggedIn === 'true' ||
        (typeof window.isLoggedIn !== 'undefined' && window.isLoggedIn);

    // Get user membership types - handle both array and string formats
    let userMembershipTypes = window.userMembershipTypes || [];
    if (typeof userMembershipTypes === 'string') {
        try {
            userMembershipTypes = JSON.parse(userMembershipTypes);
        } catch (e) {
            userMembershipTypes = [];
        }
    }

    // Debug logging (can be removed later)
    console.log('isLoggedIn:', isLoggedIn);
    console.log('userMembershipTypes:', userMembershipTypes);

    // Convert server-side Perk data to client-side format
    const perks = perksToUse.map(perk => ({
        pid: perk.pid || perk.pid,
        title: perk.title || '',
        discount: perk.discount || '',
        product: perk.product || '',
        membership: perk.membership || '',
        expiry: perk.expiryDate || perk.expiry || '',
        upvotes: perk.upvotes || 0,
        downvotes: perk.downvotes || 0
    }));

    const listDiv = document.getElementById('perk-list');
    const searchInput = document.getElementById('search');
    const membershipFilter = document.getElementById('membershipFilter');

    // Check if elements exist
    if (!listDiv || !searchInput || !membershipFilter) {
        return;
    }

    // Render the perks
    function renderList(filteredPerks) {
        listDiv.innerHTML = '';

        if (filteredPerks.length === 0) {
            listDiv.innerHTML = '<p style="text-align: center; color: #999; padding: 2rem;">No perks found matching your search.</p>';
            return;
        }

        filteredPerks.forEach(perk => {
            const div = document.createElement('div');
            div.className = 'perk-card';
            div.dataset.pid = perk.pid;

            // Check if this perk matches user's memberships (if logged in)
            // Trim and normalize strings for comparison
            const perkMembership = perk.membership ? perk.membership.trim() : '';
            const isMatching = isLoggedIn &&
                Array.isArray(userMembershipTypes) &&
                userMembershipTypes.length > 0 &&
                perkMembership !== '' &&
                userMembershipTypes.some(type => type && String(type).trim() === perkMembership);

            // Debug for first perk only
            if (perk.pid === perks[0]?.pid) {
                console.log('Checking perk:', perk.title, 'membership:', perkMembership);
                console.log('User memberships:', userMembershipTypes);
                console.log('Is matching:', isMatching);
            }

            // Add matching class for highlighting
            if (isMatching) {
                div.classList.add('perk-card-matching');
            }

            // Format expiry date for display
            const expiryDisplay = perk.expiry ? new Date(perk.expiry).toLocaleDateString() : 'N/A';

            // Add matching indicator badge if applicable
            const matchingBadge = isMatching ? '<span class="matching-badge">✓ Your Membership</span>' : '';

            div.innerHTML = `
                <h3>${perk.title} ${matchingBadge}</h3>
                <p><strong>Discount:</strong> ${perk.discount}</p>
                <p><strong>Product:</strong> ${perk.product}</p>
                <p><strong>Membership:</strong> ${perk.membership || 'N/A'}</p>
                <p><strong>Expiry:</strong> ${expiryDisplay}</p>
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

    // Filter perks based on search and membership
    function filteredPerks() {
        const searchTerm = searchInput.value.toLowerCase();
        const membershipType = membershipFilter.value;

        return perks.filter(p => {
            const matchesSearch = p.product.toLowerCase().includes(searchTerm);
            const matchesMembership = !membershipType || membershipType === '' || p.membership === membershipType;
            return matchesSearch && matchesMembership;
        });
    }

    // Initial render - always render perks (will show empty message if no perks)
    renderList(filteredPerks());

    // Update list as user types
    searchInput.addEventListener('input', () => {
        renderList(filteredPerks());
    });

    // Update list when membership filter changes
    membershipFilter.addEventListener('change', () => {
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