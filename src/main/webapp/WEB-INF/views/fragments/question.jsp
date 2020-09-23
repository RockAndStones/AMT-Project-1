<%--
  Group:        RockAndStone (https://github.com/orgs/tweb-classroom/teams/rockandstone)
  Authors:      Bécaud Arthur, Egremy Bruno, Muller Robin & Teixeira Carvalho Stéphane
  Date:         23.09.2020
  Description:  Question fragment for StoneOverflow.
--%>
<div class="question flex border-b">
    <div class="w-full flex items-start px-4 py-6">
        <img class="w-12 h-12 rounded-full object-cover mr-4 shadow" src="https://images.unsplash.com/photo-1542156822-6924d1a71ace?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60" alt="avatar">
        <div class="w-full">
            <div class="flex items-center justify-between">
                <h2 class="text-lg font-semibold text-gray-900 -mt-1">${question.title}</h2>
                <small class="text-left text-sm text-gray-700">date</small>
            </div>
            <p class="mt-3 text-gray-700 text-sm">
                ${question.shortDescription()}
            </p>
            <div class="mt-4 flex items-center">
                <div class="flex mr-2 text-gray-700 text-sm mr-3">
                    <svg fill="none" viewBox="0 0 24 24"  class="w-4 h-4 mr-1" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/>
                    </svg>
                    <span>${question.nbVotes}</span>
                </div>
                <div class="flex mr-2 text-gray-700 text-sm mr-4">
                    <span>${question.creator}</span>
                </div>
            </div>
        </div>
    </div>
</div>