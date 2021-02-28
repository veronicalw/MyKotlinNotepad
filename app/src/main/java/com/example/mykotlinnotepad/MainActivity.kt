package com.example.mykotlinnotepad

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.*
import com.example.mykotlinnotepad.models.Notes
import com.example.mykotlinnotepad.models.NotesAdapter
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlin.random.Random

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    //Objects
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var menuDrawer: ImageView
    private lateinit var addNotes: ImageView
    private lateinit var layoutDashboard : LinearLayout
    private lateinit var rvStore : RecyclerView
    private lateinit var userNames : TextView
    private lateinit var userEmails : TextView
    //Firebase
    lateinit var firestore: FirebaseFirestore
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser
    //Custom Navigation Drawer
    private var endScale : Float = 1.8f
    private lateinit var notesAsapters: FirestoreRecyclerAdapter<Notes, NotesViewHolders>
    private var TAG : String ="TAGDELETE"
    lateinit var headerView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawerLayout = findViewById(R.id.layoutDrawer)
        navigationView = findViewById(R.id.navigation_view)
        menuDrawer = findViewById(R.id.imgDrawerMenu)
        addNotes = findViewById(R.id.imgAddNotes)
        layoutDashboard = findViewById(R.id.linearMainActivity)
        rvStore = findViewById(R.id.rcViewStore)

        //Calling Firestore user
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!

        var query: Query = firestore.collection("hinotes").document(firebaseAuth.uid.toString()).collection("hiNotesUser").orderBy("titles", Query.Direction.DESCENDING)
        //Query Notes for Specific User

        var notess: FirestoreRecyclerOptions<Notes> = FirestoreRecyclerOptions.Builder<Notes>().setQuery(query, Notes::class.java).build()

        notesAsapters = object:FirestoreRecyclerAdapter<Notes, NotesViewHolders>(notess){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolders {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item_test,parent,false)
                return NotesViewHolders(view)
            }

            override fun onBindViewHolder(holder: NotesViewHolders, position: Int, notePojo: Notes) {

                holder.txtTitle.setText(notePojo.titles)
                holder.txtContent.setText(notePojo.contents)
                holder.cvNote.setCardBackgroundColor(holder.view.resources.getColor(getRandomColour(),null))
                val context = holder.txtTitle.context
                var docId = notesAsapters.snapshots.getSnapshot(position).id
                holder.view.setOnClickListener {
                    val intent = Intent(context, DetailNotesActivity::class.java)
                    intent.putExtra("titles", notePojo.titles)
                    intent.putExtra("contents", notePojo.contents)
                    intent.putExtra("id",docId)
                    context.startActivity(intent)
                }

                val imageMenu: ImageView = holder.view.findViewById(R.id.menu)
                imageMenu.setOnClickListener {
                    val popupMenu: PopupMenu = PopupMenu(it.context,it)
                    var docId = notesAsapters.snapshots.getSnapshot(position).id
                    //Pop-Up Edit
                    popupMenu.menu.add("Edit").setOnMenuItemClickListener(object:
                        MenuItem.OnMenuItemClickListener {
                        override fun onMenuItemClick(p0: MenuItem?): Boolean {
                            val intent = Intent(this@MainActivity, EditNotesActivity::class.java)
                            intent.putExtra("titles", notePojo.titles)
                            intent.putExtra("contents", notePojo.contents)
                            intent.putExtra("id",docId)
                            startActivity(intent)
                            return false
                        }
                    })
                    //Pop-Up Delete
                    popupMenu.menu.add("Delete").setOnMenuItemClickListener(object:
                        MenuItem.OnMenuItemClickListener {
                        val documentReferences: DocumentReference =
                            firestore.collection("hinotes").document(firebaseUser.uid).collection("hiNotesUser").document(docId)
                        override fun onMenuItemClick(p0: MenuItem?): Boolean {
                            documentReferences
                                .delete()
                                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!")
                                    Toast.makeText(this@MainActivity,"Successfully Deleted!", Toast.LENGTH_SHORT).show()}
                                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e)
                                    Toast.makeText(this@MainActivity,"Error Delete!", Toast.LENGTH_SHORT).show() }
                            return false
                        }
                    })
                    popupMenu.show()
                }
            }

        }
        rvStore.layoutManager = LinearLayoutManager(this)
        rvStore.layoutManager = GridLayoutManager(this,2)
        rvStore.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        rvStore.adapter = notesAsapters

        //Set Navigation Drawer Header with Textview to Display User's Name and Email
        headerView = navigationView.getHeaderView(0)
        userNames = headerView.findViewById(R.id.userNames)
        userEmails = headerView.findViewById(R.id.userEmails)
        userNames.setText(firebaseUser.displayName)
        userEmails.setText(firebaseUser.email)

        //To set if the logged user is anonymous
        if (firebaseUser.isAnonymous){
            userNames.setText("Temporary Account")
            userEmails.visibility = View.GONE
        } else {
            userNames.setText(firebaseUser.displayName)
            userEmails.setText(firebaseUser.email)
        }

//        Toast.makeText(this,"Array : " + titless.toString(), Toast.LENGTH_LONG).show()
        addNotes.setOnClickListener {
            val addNotesAct = Intent(this, AddNotesActivity::class.java)
            startActivity(addNotesAct)
        }
        navigationDrawerSetting()
    }

    private fun navigationDrawerSetting(){
        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setNavigationItemSelectedListener {
            return@setNavigationItemSelectedListener when(it.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_logout -> {
//                    FirebaseAuth.getInstance().signOut()
                    checkingUser()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_profile -> {
                    if (firebaseUser.isAnonymous){
                        val intent = Intent(this, RegisterActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "You are connected already", Toast.LENGTH_LONG).show()
//                        val inten
                    }

                    true
                }

                else -> super.onOptionsItemSelected(it)
            }
        }
        navigationView.setCheckedItem(R.id.nav_home)
        menuDrawer.setOnClickListener {
            if (drawerLayout.isDrawerVisible(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START)
            else drawerLayout.openDrawer(GravityCompat.START)
        }
        navigationDrawerAnimation()
    }

    private fun checkingUser() {
        if (firebaseUser.isAnonymous == true){
            displayAlert()
        } else {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, SplashScreenActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun displayAlert() {
        val alertDialog = AlertDialog.Builder(this).setTitle("Are you sure?")
            .setMessage("You are currently using temporary account, logging out will delete all of your data")
            .setPositiveButton("Let's Sync Your Note", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            })
            .setNegativeButton("Log Out", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    val intent = Intent(this@MainActivity, SplashScreenActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            })
        alertDialog.show()
    }

    private fun navigationDrawerAnimation() {
        drawerLayout.setScrimColor(resources.getColor(R.color.tan))
        drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener(){
            override fun onDrawerSlide(@NonNull drawerView: View, slideOffSet: Float){
                val diffScaleOffSet = slideOffSet * (1 - endScale)
                val offSetScale = 1 - diffScaleOffSet
                layoutDashboard.setScaleX(offSetScale)
                layoutDashboard.setScaleY(offSetScale)
                //Translate View to scale width
                val xOffSet = drawerView.getWidth() * slideOffSet
                val xOffSetDiff = layoutDashboard.getWidth() * diffScaleOffSet / 2
                val xTransition = xOffSet - xOffSetDiff
                layoutDashboard.setTranslationX(xTransition)
            }
        })


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }

    class NotesViewHolders (@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val txtTitle: TextView = itemView.findViewById(R.id.titleNote)
        internal val txtContent: TextView = itemView.findViewById(R.id.txtNoteContent)
        internal val cvNote: CardView = itemView.findViewById(R.id.cvView)
        internal val view: View = itemView
    }
    fun getRandomColour(): Int {
        val colours = arrayListOf<Int>()
        colours.addAll(listOf(
                R.color.cornsilk,
                R.color.blanchedalmond,
                R.color.bisque,
                R.color.navajowhite,
                R.color.wheat,
                R.color.burlywood,
                R.color.tan
        ))

        var randomColour = Random
        val number = randomColour.nextInt(colours.size)
        return colours.get(number)
    }

    override fun onStart() {
        super.onStart()
        notesAsapters.startListening()
    }

    override fun onStop() {
        super.onStop()
        if (notesAsapters != null){
            notesAsapters.stopListening()
        }
    }

}



