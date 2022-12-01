package com.example.texturestarter

import android.graphics.SurfaceTexture
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.Log
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class OpenGLRenderer(val textureAvailableCallback: (SurfaceTexture) -> Unit) : GLSurfaceView.Renderer {


    // Must negate y when calculating texcoords from vertex coords as bitmap image data assumes
    // y increases downwards
    // TODO add your texture shaders

    var texShaderProgram = -1

    var texBuffer: FloatBuffer? = null
    lateinit var texIndexBuffer: ShortBuffer

    var cameraFeedSurfaceTexture: SurfaceTexture? = null



    // We initialise the OpenGL view here
    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // Set the background colour (red=0, green=0, blue=0, alpha=1)
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // Enable depth testing - will cause nearer 3D objects to automatically
        // be drawn over further objects
        GLES20.glClearDepthf(1.0f)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

        // http://stackoverflow.com/questions/6414003/using-surfacetexture-in-android
        val GL_TEXTURE_EXTERNAL_OES = 0x8d65
        val textureId = IntArray(1)
        GLES20.glGenTextures(1, textureId, 0)
        if (textureId[0] != 0) {
            // TODO link texture ID with texture register

            // TODO add your surface texture

            // TODO compile and link your shaders

            // Create buffers for the texture rectangle
            createCameraRect()

            // TODO Send texture register 0 to the shader program

            // TODO call the texture available callback
        }
    }

    // We draw our shapes here
    override fun onDrawFrame(unused: GL10) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)


        // Camera
        GLES20.glDisable(GLES20.GL_DEPTH_TEST)

        // TODO update the texture image
        cameraFeedSurfaceTexture?.updateTexImage()

        // TODO use your shader program

        if(texBuffer == null) {
            Log.d("TextureStarterLog", "null tex buffer")
            return
        }

        val attrVarRef = GLES20.glGetAttribLocation(texShaderProgram, "aVertex")
        texBuffer?.position(0)
        texIndexBuffer.position(0)

        // TODO draw your texture rectangle

        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
    }

    // Used if the screen is resized
    override fun onSurfaceChanged(unused: GL10, w: Int, h: Int) {
        GLES20.glViewport(0, 0, w, h)
    }

    fun compileShader(shaderType: Int, shaderCode: String): Int {
        val shader = GLES20.glCreateShader(shaderType)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        return shader
    }

    fun linkShader(vertexShader: Int, fragmentShader: Int): Int {
        val shaderProgram = GLES20.glCreateProgram()
        GLES20.glAttachShader(shaderProgram, vertexShader)
        GLES20.glAttachShader(shaderProgram, fragmentShader)
        GLES20.glLinkProgram(shaderProgram)
        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(shaderProgram, GLES20.GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] == 0) {
            Log.e(
                "TextureStarterLog",
                "Error linking shader program: " + GLES20.glGetProgramInfoLog(shaderProgram)
            )
            GLES20.glDeleteProgram(shaderProgram)
            return -1
        }
        GLES20.glUseProgram(shaderProgram)
        Log.d("TextureStarterLog", "Shader program = $shaderProgram")
        return shaderProgram
    }

    // TODO add methods to create vertex and index buffers

    private fun createCameraRect() {
        // TODO - create buffers for your camera rectangle's vertices and indices by calling the buffer-creation methods above
    }
}