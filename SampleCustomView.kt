class SampleCustumView : LinearLayout {

    constructor(context: Context) :
            this(context, null)

    constructor(context: Context, attrs: AttributeSet?) :
            this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {

        LayoutInflater.from(context).inflate(R.layout.{id_ressource}, this, true)

        /*
        #Solution 1
        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(attrs, R.styleable.SampleCustumView, 0, 0)
            try {
            ...
            } finally {
                a.recycle()
            }
        }

       #Solution 2
        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(attrs, R.styleable.SampleCustumView, 0, 0)
            try {
                for (i in 0 until a.indexCount) {
                    val attr = a.getIndex(i)
                    when (attr) {
                        R.styleable.SampleCustumView_image1 -> {
                            image.setImageDrawable(a.getDrawable(attr))
                        }
                        R.styleable.SampleCustumView_name1 -> {
                            description.text = a.getString(attr)
                        }
                        R.styleable.SampleCustumView_name2 -> {
                            title.text = a.getString(attr)
                        }
                    }
                }
            } finally {
                a.recycle()
            }
        }
        */
    }

    //...
}
