import numpy as np
import librosa
import array as arr


means = [3.79534063e-01,8.48761481e-02,1.30859051e-01,2.67638762e-03,
         2.19921943e+03,4.16672699e+05,2.24138596e+03,1.18271113e+05,
         4.56607659e+03,1.62878997e+06,1.02578486e-01,2.62012081e-03,
        -3.64630510e-04,1.25975714e-02,-3.95501617e-04,5.60155268e-03,
         1.24887709e+02]

vars = [8.18323495e-03,9.28551851e-05,4.69800060e-03,1.28554416e-05,
       5.65237792e+05,1.89175122e+11,2.95748054e+05,1.02708882e+10,
       2.69610866e+06,2.21808498e+12,2.08382222e-03,1.30565117e-05,
       2.88780387e-06,1.59582813e-04,1.22714766e-06,4.42609669e-05,
       1.08307001e+03]


def scale_data(array,means=means,stds=np.power(vars,.5)):
    return (array-means)/stds

def getArray():
    audio_data = 'storage/emulated/0/Android/data/com.example.spice/files/audio.wav'

    y, sr = librosa.load(audio_data) # sample rate can be changed here, y= signal
    arr1 = arr.array('f')

    chroma_stft = librosa.feature.chroma_stft(y=y,sr=sr)
    chroma_stft_mean = np.mean(chroma_stft)
    arr1.insert(0,chroma_stft_mean) # insert first variable into array

    chroma_stft_var = np.var(chroma_stft)
    arr1.insert(1,chroma_stft_var)

    rms = librosa.feature.rms(y=y)
    rms_mean = np.mean(rms)
    arr1.insert(2,rms_mean)

    rms_var = np.var(rms)
    arr1.insert(3,rms_var)

    spec_centroid = librosa.feature.spectral_centroid(y=y,sr = sr)
    spec_centroid_mean = np.mean(spec_centroid)
    arr1.insert(4,spec_centroid_mean)

    spec_centroid_var = np.var(spec_centroid)
    arr1.insert(5,spec_centroid_var)

    spec_bw = librosa.feature.spectral_bandwidth(y=y,sr=sr)
    spec_bw_mean = np.mean(spec_bw)
    arr1.insert(6,spec_bw_mean)

    spec_bw_var = np.var(spec_bw)
    arr1.insert(7,spec_bw_var)

    rolloff = librosa.feature.spectral_rolloff(y=y, sr =sr)
    #rolloff is a measure of the shape of the signal.
    rolloff_mean = np.mean(rolloff)
    arr1.insert(8,rolloff_mean)

    rolloff_var = np.var(rolloff)
    arr1.insert(9,rolloff_var)

    zcr = librosa.feature.zero_crossing_rate(y=y)
    #The zero crossing rate is the rate of sign-changes along a signal
    zcr_mean = np.mean(zcr)
    arr1.insert(10,zcr_mean)

    zcr_var = np.var(zcr)
    arr1.insert(11,zcr_var)

    harmony = librosa.effects.harmonic(y=y)
    harmony_mean = np.mean(harmony)
    arr1.insert(12,harmony_mean)

    harmony_var = np.var(harmony)
    arr1.insert(13,harmony_var)

    perceptrual = librosa.effects.hpss(y=y)
    perceptrual_mean = np.mean(perceptrual)
    arr1.insert(14, perceptrual_mean)

    perceptrual_var = np.var(perceptrual)
    arr1.insert(15, perceptrual_var)

    tempo = librosa.beat.tempo(y=y, sr=sr)
    #tempo_mean = np.mean(tempo)     - tempo only has 1 variable
    arr1.insert(16,tempo)

    newArray= np.array(arr1, dtype=float)
    newArray = scale_data(newArray)
    return newArray
