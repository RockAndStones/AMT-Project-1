<%--
  Group:        RockAndStone (https://github.com/orgs/tweb-classroom/teams/rockandstone)
  Authors:      Bécaud Arthur, Egremy Bruno, Muller Robin & Teixeira Carvalho Stéphane
  Date:         23.09.2020
  Description:  Post fragment of question/answer for StoneOverflow.
--%>

<!-- Vote -->
<div class="flex flex-col inline-flex items-center">
    <form action="" method="post">
        <input type="hidden" name="type" value="question">
        <input type="hidden" name="id" value="${post.id}">
        <input type="hidden" name="vote" value="up">
        <button type="submit"><i class="leading-normal fas fa-caret-up text-6xl"></i></button>
    </form>
    <span class="text-lg">${post.nbVotes}TMP:2</span>
    <form action="" method="post">
        <input type="hidden" name="type" value="question">
        <input type="hidden" name="id" value="${post.id}">
        <input type="hidden" name="vote" value="down">
        <button type="submit"><i class="leading-normal fas fa-caret-down text-6xl"></i></button>
    </form>
</div>
<!-- Post content -->
<div class="ml-6 mt-4 text-gray-700">
    <p>
        ${post.description}
        TMP: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc accumsan vel urna sed lobortis. Nulla lectus est, posuere sed leo vel, elementum tristique nunc. Fusce sit amet magna odio. Mauris vel odio tincidunt, facilisis tortor euismod, porta sapien. Sed et ex quis turpis sodales laoreet. Mauris ac nisl id nunc consectetur facilisis quis a nulla. Fusce fermentum risus gravida volutpat euismod. Cras magna lorem, semper eu mi eget, iaculis tincidunt ipsum. Vivamus sagittis leo non posuere hendrerit. Donec efficitur libero non egestas ultrices. Pellentesque tincidunt viverra erat faucibus iaculis. Praesent volutpat, ipsum quis tristique imperdiet, mauris eros posuere ligula, nec sodales nulla lorem eu nisi. Duis viverra mi eu mauris condimentum, id condimentum justo suscipit. Vivamus consequat porttitor rhoncus. Donec sapien mi, viverra mollis volutpat ut, aliquet et nunc.
    </p>
    <span class="inline-block text-sm font-semibold mt-4">${post.creator} TMP: CREATOR, ${post.date}TMP: DATE</span>
</div>
